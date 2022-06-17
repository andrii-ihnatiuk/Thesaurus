package com.opsu.thesaurus.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.opsu.thesaurus.R
import com.opsu.thesaurus.fragments.GameResultFragment

class GameResultAdapter(
    context: Context,
    private val gameResult: GameResultFragment.GameResult
): RecyclerView.Adapter<GameResultAdapter.GameResultViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    class GameResultViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtTerm: TextView = view.findViewById(R.id.txtTerm)
        val txtDefinition: TextView = view.findViewById(R.id.txtTermDefinition)

        val imgStatus: ImageView = view.findViewById(R.id.imgResultStatus)
        val txtStatus: TextView = view.findViewById(R.id.txtResultStatus)
        val container: LinearLayout = view.findViewById(R.id.statusContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameResultViewHolder {
        return GameResultViewHolder(inflater.inflate(
            R.layout.game_result_list_item,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: GameResultViewHolder, position: Int) {
        holder.txtTerm.text = gameResult.combinations[position].term
        holder.txtDefinition.text = gameResult.combinations[position].definition
        holder.txtStatus.text =
            if (gameResult.userAnswers[position]) holder.itemView.resources.getString(R.string.game_correct)
            else holder.itemView.resources.getString(R.string.game_incorrect)

        Log.d("userAnswers", gameResult.userAnswers[position].toString())

        Log.d("areCorrect", gameResult.areCorrectAnswers[position].toString())

        if (gameResult.areCorrectAnswers[position])
        {
            holder.imgStatus.setImageResource(R.drawable.ic_done)
            holder.container.setBackgroundResource(R.color.correct_answer)
        }
        else
        {
            holder.imgStatus.setImageResource(R.drawable.ic_cancel_cross)
            holder.container.setBackgroundResource(R.color.incorrect_answer)
        }
    }

    override fun getItemCount(): Int = gameResult.combinations.size
}