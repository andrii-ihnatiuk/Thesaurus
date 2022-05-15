package com.opsu.thesaurus.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

        holder.termListener.updateBindings(holder)
        holder.defListener.updateBindings(holder)

        holder.term.setText(term.term)
        holder.definition.setText(term.definition)
    }

    override fun getItemCount() = terms.size


    class TermEditTextListener(termsList: MutableList<DataModels.TermModel>) : TextWatcher
    {
        private var terms = termsList
        private var position = 0
        private lateinit var container: TextInputLayout

        fun updateBindings(holder: ViewHolder)
        {
            this.position = holder.adapterPosition
            container = holder.itemView.findViewById(R.id.editTermContainer)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {
            terms[position].term = p0.toString()
            if (container.error != "")
                container.error = ""
        }
        override fun afterTextChanged(p0: Editable?) { }
    }

    class DefinitionEditTextListener(termsList: MutableList<DataModels.TermModel>) : TextWatcher
    {
        private val terms = termsList
        private var position = 0
        private lateinit var container: TextInputLayout

        fun updateBindings(holder: ViewHolder)
        {
            this.position = holder.adapterPosition
            container = holder.itemView.findViewById(R.id.editDefinitionContainer)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {
            terms[position].definition = p0.toString()
            if (container.error != "")
                container.error = ""
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

}