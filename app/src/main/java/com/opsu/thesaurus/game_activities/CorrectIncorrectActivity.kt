package com.opsu.thesaurus.game_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.databinding.ActivityCorrectIncorrectBinding
import com.opsu.thesaurus.fragments.GameResultFragment
import kotlin.random.Random

class CorrectIncorrectActivity : AppCompatActivity()
{
    private var activeTermId = 0
    private var isCorrectDisplayed = false
    private var correctAnswerCount = 0
    private var correctIndices: MutableList<Int> = mutableListOf()

    private var terms: ArrayList<Entities.Term> = arrayListOf()

    private lateinit var binding: ActivityCorrectIncorrectBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCorrectIncorrectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.apply {
            this.toolbarCancel.setOnClickListener {
                onBackPressed()
            }
            this.txtGameTitle.text = resources.getString(R.string.card_game3)
            this.progressIndicator.progress = 0
            this.progressIndicator.max = terms.size
        }

        terms = intent.extras?.getParcelableArrayList<Entities.Term>("terms") as ArrayList<Entities.Term>
        terms.shuffle()

        binding.btnCorrect.setOnClickListener {
            binding.toolbar.progressIndicator.progress += 1
            checkUserChoice(true)
            bindFields()
        }
        binding.btnIncorrect.setOnClickListener {
            binding.toolbar.progressIndicator.progress += 1
            checkUserChoice(false)
            bindFields()
        }

        supportFragmentManager.setFragmentResultListener("gameFinish", this) {_, result ->
            val dialogRes = result.get("continue") as Boolean
            if (dialogRes)
            {
                binding.toolbar.progressIndicator.progress = 0
                terms.shuffle()
                activeTermId = 0
                correctAnswerCount = 0
                correctIndices.clear()
                bindFields()
            }
            else
                finish()
        }

        bindFields()
    }

    private fun bindFields()
    {
        val nextTerm = terms[activeTermId].term
        var nextDefinition = ""

        if (Random.nextInt(0, 2) == 1)
        {
            nextDefinition = terms[activeTermId].definition
            isCorrectDisplayed = true
        } else {
            var randomId = activeTermId
            while(randomId == activeTermId)
                randomId = Random.nextInt(0, terms.size)
            nextDefinition = terms[randomId].definition
            isCorrectDisplayed = false
        }

        binding.txtTerm.text = nextTerm
        binding.txtDefinition.text = nextDefinition

        activeTermId += 1
    }

    private fun checkUserChoice(userInput: Boolean)
    {
        if (isCorrectDisplayed == userInput)
        {
            correctAnswerCount += 1
            correctIndices.add(activeTermId-1)
        }

        if (binding.toolbar.progressIndicator.progress == binding.toolbar.progressIndicator.max)
            showResultWindow()
        else
            bindFields()
    }

    private fun showResultWindow()
    {
        val fragment = GameResultFragment(correctAnswerCount, terms.size)
        fragment.submitCorrectIndices(terms, correctIndices)

        fragment.show(supportFragmentManager)
    }
}