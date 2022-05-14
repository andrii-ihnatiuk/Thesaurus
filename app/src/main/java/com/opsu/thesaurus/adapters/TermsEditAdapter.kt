package com.opsu.thesaurus.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.opsu.thesaurus.R
import com.opsu.thesaurus.models.DataModels

class TermsEditAdapter(
    private val inflater: LayoutInflater, private val terms: MutableList<DataModels.TermModel>
    ) : RecyclerView.Adapter<TermsEditAdapter.ViewHolder>()
{

    class ViewHolder(
        view: View, termTextLister: TermEditTextListener, defTextListener: DefinitionEditTextListener
        ) : RecyclerView.ViewHolder(view)
    {
        val termListener = termTextLister
        val defListener = defTextListener

        val term: TextInputEditText = view.findViewById<TextInputEditText?>(R.id.txtEditTerm).also {
            it.addTextChangedListener(termListener)
        }
        val definition: TextInputEditText = view.findViewById<TextInputEditText?>(R.id.txtEditDefinition).also {
            it.addTextChangedListener(defListener)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(inflater.inflate(
            R.layout.terms_edit_list_item,
            parent,
            false), TermEditTextListener(terms), DefinitionEditTextListener(terms)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val term = terms[position]

        holder.term.setText(term.term)
        holder.definition.setText(term.definition)

        holder.termListener.updatePosition(holder.adapterPosition)
        holder.defListener.updatePosition(holder.adapterPosition)
    }

    override fun getItemCount() = terms.size

    fun getData(): MutableList<DataModels.TermModel> = terms

    class TermEditTextListener(termsList: MutableList<DataModels.TermModel>) : TextWatcher
    {
        private var terms = termsList
        private var position = 0

        fun updatePosition(position: Int) {
            this.position = position
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            terms[position].term = p0.toString()
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    class DefinitionEditTextListener(termsList: MutableList<DataModels.TermModel>) : TextWatcher
    {
        private val terms = termsList
        private var position = 0

        fun updatePosition(position: Int) {
            this.position = position
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            terms[position].definition = p0.toString()
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

}