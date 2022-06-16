package com.opsu.thesaurus.game_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.databinding.ActivityLearnBinding
import com.opsu.thesaurus.fragments.GameResultFragment
import com.opsu.thesaurus.fragments.dialogs.GameCorrectAnswerDialog
import com.opsu.thesaurus.fragments.dialogs.GameWrongAnswerDialog
import kotlin.random.Random

class LearnActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLearnBinding
    private var terms: ArrayList<Entities.Term> = arrayListOf()
    private var activeTermId = 0
    private var correctAnswerId = 0
    private var correctAnswers = 0

    private lateinit var views: ArrayList<TextView>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.toolbarCancel.setOnClickListener {
            onBackPressed()
        }

        terms = intent.extras?.getParcelableArrayList<Entities.Term>("terms") as ArrayList<Entities.Term>
        terms.shuffle()

        binding.toolbar.txtGameTitle.text = resources.getString(R.string.card_game1)
        binding.toolbar.imgGameTitle.setImageResource(R.drawable.ic_rotate)
        binding.toolbar.progressIndicator.apply {
            this.max = terms.size
            this.progress = 0
        }

        views = arrayListOf(
            binding.firstDef, binding.secondDef,
            binding.thirdDef, binding.fourthDef
        )

        hideTextViews()
        bindDefinitions()

        views.forEach { textView ->
            textView.setOnClickListener {
                try {
                    checkUserChoice(Integer.parseInt(it.tag.toString()))
                } catch (e: NumberFormatException) { }
            }
        }

        supportFragmentManager.setFragmentResultListener("requestContinue", this) { _, _ ->
            binding.toolbar.progressIndicator.progress += 1
            if (binding.toolbar.progressIndicator.progress == binding.toolbar.progressIndicator.max)
                showResultWindow()
            else
                bindDefinitions()
        }

        supportFragmentManager.setFragmentResultListener("gameFinish", this) {_, result ->
            val dialogRes = result.get("continue") as Boolean
            if (dialogRes)
            {
                binding.toolbar.progressIndicator.progress = 0
                terms.shuffle()
                activeTermId = 0
                correctAnswers = 0
                bindDefinitions()
            }
            else
                finish()
        }
    }

    private fun showResultWindow()
    {
        val fragment = GameResultFragment(correctAnswers, terms.size)
        fragment.show(supportFragmentManager)
    }

    private fun hideTextViews()
    {
        if (terms.size < 4)
        {
            val numToHide = views.size - terms.size

            for (i in 0 until numToHide)
            {
                views.last().visibility = LinearLayout.GONE
                views.removeLast()
            }
        }
    }

    private fun bindDefinitions()
    {
        binding.txtTerm.text = terms[activeTermId].term
        val randomIndices =  (0 until terms.size).shuffled().take(4)

        for (i in 0 until views.size)
            views[i].text = terms[randomIndices[i]].definition

        correctAnswerId = if (randomIndices.contains(activeTermId)) randomIndices.indexOf(activeTermId) else {
            val id = Random.nextInt(0, views.size)
            views[id].text = terms[activeTermId].definition
            id
        }

        activeTermId+=1
    }

    private fun checkUserChoice(clickedPos: Int)
    {
        if (clickedPos == correctAnswerId)
        {
            correctAnswers+=1
            val dialog = GameCorrectAnswerDialog()
            dialog.show(supportFragmentManager, "choiceResult")
        }
        else
        {
            val term = terms[activeTermId-1]
            val dialog = GameWrongAnswerDialog(term.term, term.definition, views[clickedPos].text.toString())
            dialog.show(supportFragmentManager, "choiceResult")
        }
    }

}