package com.opsu.thesaurus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.opsu.thesaurus.R
import com.opsu.thesaurus.adapters.GameResultAdapter
import com.opsu.thesaurus.database.entities.Entities
import kotlin.math.abs

class GameResultFragment(private val correct: Int, private val total: Int) : Fragment()
{
    private var gameResult: GameResult? = null

    private lateinit var vp2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_game_result, container, false)

        view.findViewById<MaterialButton>(R.id.btnFinish).setOnClickListener {
            setFragmentResult("gameFinish", bundleOf("continue" to false))
            activity?.supportFragmentManager?.popBackStack()
        }
        view.findViewById<MaterialButton>(R.id.btnLearnAgain).setOnClickListener {
            setFragmentResult("gameFinish", bundleOf("continue" to true))
            activity?.supportFragmentManager?.popBackStack()
        }
        val txtCorrectCount = view.findViewById<TextView>(R.id.txtCorrectCount)
        txtCorrectCount.text = getString(R.string.game_result_info, correct, total)

        if (correct.toFloat() / total < 0.5)
        {
            view.findViewById<ImageView>(R.id.imgGameResult).setImageResource(R.drawable.ic_confused)
            view.findViewById<TextView>(R.id.txtGameResultTitle).text = getString(R.string.game_result_title_bad)
            txtCorrectCount.setTextColor(ContextCompat.getColor(requireContext(), R.color.incorrect_answer))
        }

        gameResult?.let {
            val recyclerAdapter = GameResultAdapter(requireContext(), it)

            vp2 = view.findViewById(R.id.gameResultList)
            vp2.adapter = recyclerAdapter
            vp2.offscreenPageLimit = 3
            vp2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val pageTransformer = CompositePageTransformer()
            pageTransformer.addTransformer(MarginPageTransformer(20))
            pageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.9f + r * 0.1f
            }
            vp2.setPageTransformer(pageTransformer)
        }

        return view
    }

    fun submitGameResult(result: GameResult)
    {
        this.gameResult = result
    }

    fun show(fragmentManager: FragmentManager)
    {
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction
            .add(android.R.id.content, this)
            .addToBackStack(null)
            .commit()
    }

    data class GameResult(
        var combinations: MutableList<Entities.Term>, // комбінації термін-визначення які були показані користувачу
        var userAnswers: MutableList<Boolean>, // відповіді користувача True/False
        var areCorrectAnswers: MutableList<Boolean> // чи є відповідь користувача правильною
    )
}