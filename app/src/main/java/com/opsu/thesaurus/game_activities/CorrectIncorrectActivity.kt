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

    private var terms: ArrayList<Entities.Term> = arrayListOf()
    private var gameResult = GameResultFragment.GameResult(mutableListOf(), mutableListOf(), mutableListOf())

    private lateinit var binding: ActivityCorrectIncorrectBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCorrectIncorrectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        terms = intent.extras?.getParcelableArrayList<Entities.Term>("terms") as ArrayList<Entities.Term>
        terms.shuffle()

        binding.toolbar.apply {
            this.toolbarCancel.setOnClickListener {
                onBackPressed()
            }
            this.txtGameTitle.text = resources.getString(R.string.card_game3)
            this.imgGameTitle.setImageResource(R.drawable.ic_checkmark)
            this.progressIndicator.progress = 0
            this.progressIndicator.max = terms.size
        }

        binding.btnCorrect.setOnClickListener {
            binding.toolbar.progressIndicator.progress += 1
            checkUserChoice(true)
        }
        binding.btnIncorrect.setOnClickListener {
            binding.toolbar.progressIndicator.progress += 1
            checkUserChoice(false)
        }

        supportFragmentManager.setFragmentResultListener("gameFinish", this) {_, result ->
            val dialogRes = result.get("continue") as Boolean
            if (dialogRes)
            {
                binding.toolbar.progressIndicator.progress = 0
                terms.shuffle()
                activeTermId = 0
                correctAnswerCount = 0
                gameResult.apply {
                    this.combinations.clear()
                    this.areCorrectAnswers.clear()
                    this.userAnswers.clear()
                }
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
        val nextDefinition: String

        if (Random.nextInt(0, 2) == 1)
        {
            nextDefinition = terms[activeTermId].definition
            isCorrectDisplayed = true
        }
        else {
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
            correctAnswerCount += 1

        gameResult.combinations.add(Entities.Term(binding.txtTerm.text.toString(), binding.txtDefinition.text.toString()))
        gameResult.userAnswers.add(userInput)
        gameResult.areCorrectAnswers.add(userInput == isCorrectDisplayed)

        if (binding.toolbar.progressIndicator.progress == binding.toolbar.progressIndicator.max)
            showResultWindow()
        else
            bindFields()
    }

    private fun showResultWindow()
    {
        val fragment = GameResultFragment(correctAnswerCount, terms.size)
        fragment.submitGameResult(gameResult)

        fragment.show(supportFragmentManager)
    }
}