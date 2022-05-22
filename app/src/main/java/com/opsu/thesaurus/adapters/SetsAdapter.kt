package com.opsu.thesaurus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities

class SetsAdapter(
    private val inflater: LayoutInflater
    ) : ListAdapter<Entities.Set, SetsAdapter.ViewHolder>(AsyncDifferConfig.Builder(DiffCallback()).build())
{
    private lateinit var context: Context
    private lateinit var mListener: ItemClickListener

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }


    class ViewHolder(view: View, clickListener: ItemClickListener) : RecyclerView.ViewHolder(view)
    {
        val setName: TextView = view.findViewById(R.id.txtSetName)
        val termsCount: TextView = view.findViewById(R.id.txtTermsCount)
        val author:TextView = view.findViewById(R.id.txtSetAuthor)

        init {
            view.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Entities.Set>()
    {
        override fun areItemsTheSame(oldItem: Entities.Set, newItem: Entities.Set): Boolean {
            return oldItem.setTitle == newItem.setTitle // comparing unique id in table
        }
        // called if areItemsTheSame == true
        override fun areContentsTheSame(oldItem: Entities.Set, newItem: Entities.Set): Boolean {
            return oldItem.createdBy == newItem.createdBy && oldItem.numOfTerms == newItem.numOfTerms
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        context = parent.context
        return ViewHolder(inflater.inflate(
                R.layout.sets_list_item,
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val set = getItem(position)

        holder.setName.text = set.setTitle
        holder.termsCount.text = context.getString(R.string.terms_count, set.numOfTerms)
        holder.author.text = set.createdBy
    }

    fun setOnItemClickLister(lister: ItemClickListener)
    {
        mListener = lister
    }

}