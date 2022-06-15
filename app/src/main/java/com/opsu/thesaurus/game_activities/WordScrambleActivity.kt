package com.opsu.thesaurus.game_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.databinding.ActivityWordScrambleBinding
import kotlin.random.Random

class WordScrambleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordScrambleBinding
    var maxAttemtps : Int = 3;

    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityWordScrambleBinding.inflate(layoutInflater)
            setContentView(binding.root)
            // початок гри
            startGame()
        }

    // функція, яка підготовує дане Activity до початку гри
    private fun startGame() {
        var currentAttempts = 0;
        // завантаження слова з ресурсів
        val sWord = getWord()
        // перемішування завантаженого слова
        val mixedWord = mixWord(sWord)
        binding.scrambledWord.text = mixedWord

        binding.buttonUnscramble.setOnClickListener {
            val userWord = binding.userInput.text.toString() ?: ""
            if (userWord.uppercase() == sWord.uppercase()) {
                Toast.makeText(this, "You guessed!", Toast.LENGTH_SHORT).show()
                binding.userInput.text.clear()
                startGame()
            }
            else {
                Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show()
                currentAttempts += 1;
            }
        }
    }

    // фукнкція для отримання випадкового слова з масиву рядків
    private fun getWord(): String {

        val terms =
            intent.extras?.getParcelableArrayList<Entities.Term>("terms") as ArrayList<Entities.Term>

        return terms[Random.nextInt(terms.size)].term
    }

    // функція для перемішування рядку
    private fun mixWord(word: String): String {

        val shuffledWord = word.toCharArray().let {
            it.shuffle()
            it.concatToString()
        }

        return shuffledWord
    }

}

