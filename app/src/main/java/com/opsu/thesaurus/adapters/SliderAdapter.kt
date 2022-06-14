package com.opsu.thesaurus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
    private lateinit var mListener: ItemClickListener

    interface ItemClickListener {
        fun onItemClick(frontCard: FrameLayout, backCard: FrameLayout, isFront: Boolean)
    }

    class SliderViewHolder(view: View, listener: ItemClickListener): RecyclerView.ViewHolder(view)
    {
        val frontCard: FrameLayout = view.findViewById(R.id.cardFront)
        val backCard: FrameLayout = view.findViewById(R.id.cardBack)

        val txtTerm: TextView = view.findViewById(R.id.txtSliderTerm)
        val txtDefinition: TextView = view.findViewById(R.id.txtSliderDefinition)

        init {
            view.setOnClickListener {
                if (frontCard.alpha == 1F)
                    listener.onItemClick(frontCard, backCard, true)
                else
                    listener.onItemClick(frontCard, backCard, false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(inflater.inflate(
            R.layout.term_slider_item,
            parent,
            false
        ), mListener)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.txtTerm.text = currentList[position].term
        holder.txtDefinition.text = currentList[position].definition

        holder.frontCard.alpha = 1F
        holder.backCard.alpha = 0F
    }

    override fun getItemCount(): Int = currentList.size

    fun setOnItemClickListener(listener: ItemClickListener)
    {
        mListener = listener
    }

}