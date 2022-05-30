package com.opsu.thesaurus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities

class SliderAdapter(
    private val inflater: LayoutInflater
): ListAdapter<Entities.Term, SliderAdapter.SliderViewHolder>(AsyncDifferConfig.Builder(TermsAdapter.DiffCallback()).build())
{

    class SliderViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtItem: TextView = view.findViewById(R.id.txtSliderItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(inflater.inflate(
            R.layout.term_slider_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.txtItem.text = currentList[position].term
    }

    override fun getItemCount(): Int = currentList.size

}