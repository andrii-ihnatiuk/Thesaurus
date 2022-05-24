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
import com.opsu.thesaurus.database.entities.Entities

class TermsEditAdapter(
    private val inflater: LayoutInflater, private val terms: MutableList<Entities.Term>
    ) : RecyclerView.Adapter<TermsEditAdapter.ViewHolder>()
{
    companion object {
        const val FIELD_REQUIRED = "This field is required"
        const val TITLE_NOT_SET = "Must specify title"
    }

    var errPositions: MutableList<ArrayList<Boolean>> = MutableList(terms.size) {
        arrayListOf(false, false)
    }

    class ViewHolder(
        view: View, termTextLister: TermEditTextListener, defTextListener: DefinitionEditTextListener
        ) : RecyclerView.ViewHolder(view) {

        val termListener = termTextLister
        val defListener = defTextListener

        val editTermContainer: TextInputLayout = view.findViewById(R.id.editTermContainer)
        val editDefinitionContainer: TextInputLayout = view.findViewById(R.id.editDefinitionContainer)

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
            false), TermEditTextListener(terms, errPositions), DefinitionEditTextListener(terms, errPositions)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val term = terms[position]

        holder.termListener.updateBindings(holder)
        holder.defListener.updateBindings(holder)

        holder.term.setText(term.term)
        holder.definition.setText(term.definition)

        // Setting errors for containers

        if (errPositions[position][0]) { // will be true when has error set
            if (holder.editTermContainer.error != "") // if error not set to holder yet
                holder.editTermContainer.error = FIELD_REQUIRED
        }
        else if (holder.editTermContainer.error != ""){ // if error set remove it
            holder.editTermContainer.error = ""
        }

        if (errPositions[position][1]) {
            if (holder.editDefinitionContainer.error != "")
                holder.editDefinitionContainer.error = FIELD_REQUIRED
        }
        else if (holder.editDefinitionContainer.error != "")
            holder.editDefinitionContainer.error = ""
    }

    override fun getItemCount() = terms.size


    class TermEditTextListener(
        private val terms: MutableList<Entities.Term>,
        private val errPositions: MutableList<ArrayList<Boolean>>
    ) : TextWatcher {

        private var position = 0
        private lateinit var container: TextInputLayout

        fun updateBindings(holder: ViewHolder)
        {
            this.position = holder.adapterPosition
            container = holder.editTermContainer
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {
            terms[position].term = p0.toString()
            if (container.error != "" && terms[position].term.isNotBlank())
            {
                container.error = ""
                errPositions[position][0] = false // now this field is not empty so no errors
            }

        }
        override fun afterTextChanged(p0: Editable?) { }
    }

    class DefinitionEditTextListener(
        private val terms: MutableList<Entities.Term>,
        private val errPositions: MutableList<ArrayList<Boolean>>
    ) : TextWatcher {

        private var position = 0
        private lateinit var container: TextInputLayout

        fun updateBindings(holder: ViewHolder)
        {
            this.position = holder.adapterPosition
            container = holder.editDefinitionContainer
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {
            terms[position].definition = p0.toString()
            if (container.error != "" && terms[position].definition.isNotBlank())
            {
                container.error = ""
                errPositions[position][1] = false // now this field is not empty so no errors
            }
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    // if new term added increase size of array for tracking input
    fun notifyNewTermInserted(position: Int)
    {
        errPositions.add(arrayListOf(false, false))
        this.notifyItemInserted(position)
    }



}