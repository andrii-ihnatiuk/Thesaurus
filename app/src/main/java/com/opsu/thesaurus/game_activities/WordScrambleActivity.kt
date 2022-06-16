package com.opsu.thesaurus.game_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.databinding.ActivityWordScrambleBinding
import com.opsu.thesaurus.fragments.dialogs.GameCorrectAnswerDialog
import com.opsu.thesaurus.fragments.dialogs.GameWrongAnswerDialog
import kotlin.random.Random

class WordScrambleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordScrambleBinding
    private var currentIteration = 0
    private var correctAnswers = 0
    private var currentWord: String = ""
    private var terms: ArrayList<Entities.Term> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityWordScrambleBinding.inflate(layoutInflater)
            setContentView(binding.root)

            terms = intent.extras?.getParcelableArrayList<Entities.Term>("terms") as ArrayList<Entities.Term>
            terms.shuffle()

            binding.toolbar.toolbarCancel.setOnClickListener {
                onBackPressed()
            }
            binding.toolbar.txtGameTitle.text = resources.getString(R.string.card_game2)
            binding.toolbar.imgGameTitle.setImageResource(R.drawable.ic_wordscramble)

            binding.toolbar.progressIndicator.apply {
                this.max = terms.size
                this.progress = 0
            }

            startGame()
        }


    private fun startGame() {

        // завантаження слова з ресурсів
        val sWord = terms[currentIteration].term

        // перемішування завантаженого слова
        val mixedWord = mixWord(sWord)
        binding.scrambledWord.text = mixedWord

        binding.buttonUnscramble.setOnClickListener {
            val userWord = binding.userInput.text.toString() ?: ""
            if (userWord.lowercase() == sWord.lowercase()) {
                correctAnswers+=1
                val dialog = GameCorrectAnswerDialog()
                dialog.show(supportFragmentManager, "choiceResult")
                binding.userInput.text.clear()
                binding.toolbar.progressIndicator.progress += 1
                checkForFinish()
            }
            else {
                val dialog = GameWrongAnswerDialog(sWord, sWord, userWord)
                dialog.show(supportFragmentManager, "choiceResult")
                binding.userInput.text.clear()
                binding.toolbar.progressIndicator.progress += 1
                checkForFinish()
            }
        }
        currentIteration += 1
    }

    private fun checkForFinish() {
        if (currentIteration != terms.size) {
            startGame()
        } else {
            binding.buttonUnscramble.isClickable = false
        }
    }

    private fun mixWord(word: String): String {

        val shuffledWord = word.toCharArray().let {
            it.shuffle()
            it.concatToString()
        }
        return shuffledWord
    }


}

