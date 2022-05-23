package com.opsu.thesaurus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities

class TermsAdapter(
    private val inflater: LayoutInflater, private val list: List<Entities.Term>
): RecyclerView.Adapter<TermsAdapter.TermsViewHolder>()
{
    class TermsViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtTerm: TextView = view.findViewById(R.id.txtTerm)
        val txtDefinition: TextView = view.findViewById(R.id.txtTermDefinition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermsViewHolder {
        return TermsViewHolder(inflater.inflate(
            R.layout.term_list_item,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {
        holder.txtTerm.text = list[position].term
        holder.txtDefinition.text = list[position].definition
    }

    override fun getItemCount(): Int = list.size


    class NoScrollLinearLayoutManager(private val context: Context): LinearLayoutManager(context)
    {
        override fun canScrollVertically(): Boolean = false
    }
}