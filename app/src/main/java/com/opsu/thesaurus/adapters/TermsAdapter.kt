package com.opsu.thesaurus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities

class TermsAdapter(
    private val inflater: LayoutInflater
): ListAdapter<Entities.Term, TermsAdapter.TermsViewHolder>(AsyncDifferConfig.Builder(DiffCallback()).build())
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

    class DiffCallback : DiffUtil.ItemCallback<Entities.Term>()
    {
        override fun areItemsTheSame(oldItem: Entities.Term, newItem: Entities.Term): Boolean {
            return oldItem.termId == newItem.termId // comparing unique id in a table
        }
        // called if areItemsTheSame == true
        override fun areContentsTheSame(oldItem: Entities.Term, newItem: Entities.Term): Boolean {
            return oldItem.term == newItem.term && oldItem.definition == newItem.definition
        }
    }

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {
        holder.txtTerm.text = currentList[position].term
        holder.txtDefinition.text = currentList[position].definition
    }

    override fun getItemCount(): Int = currentList.size


    class NoScrollLinearLayoutManager(private val context: Context): LinearLayoutManager(context)
    {
        override fun canScrollVertically(): Boolean = false
    }
}