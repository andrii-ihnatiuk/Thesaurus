package com.opsu.thesaurus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities

class GameResultAdapter(
    context: Context,
    private val list: List<Entities.Term>,
    private val correctIndices: List<Int>
): RecyclerView.Adapter<GameResultAdapter.GameResultViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    class GameResultViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtTerm: TextView = view.findViewById(R.id.txtTerm)
        val txtDefinition: TextView = view.findViewById(R.id.txtTermDefinition)

        val imgStatus: ImageView = view.findViewById(R.id.imgResultStatus)
        val txtStatus: TextView = view.findViewById(R.id.txtResultStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameResultViewHolder {
        return GameResultViewHolder(inflater.inflate(
            R.layout.game_result_list_item,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: GameResultViewHolder, position: Int) {
        holder.txtTerm.text = list[position].term
        holder.txtDefinition.text = list[position].definition

        if (correctIndices.contains(holder.adapterPosition))
        {

        }
    }

    override fun getItemCount(): Int = list.size
}