package com.opsu.thesaurus.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.opsu.thesaurus.R
import com.opsu.thesaurus.database.entities.Entities

class TermsEditAdapter(
    private val inflater: LayoutInflater
    ) : ListAdapter<Entities.Term, TermsEditAdapter.ViewHolder>(AsyncDifferConfig.Builder(TermsAdapter.DiffCallback()).build())
{
    companion object {
        const val FIELD_REQUIRED = "This field is required"
        const val TITLE_NOT_SET = "Must specify title"
    }

    private var deleteListener: IconDeleteClickListener? = null

    interface IconDeleteClickListener
    {
        fun onDeleteIconClick(position: Int)
    }

    lateinit var errPositions: MutableList<ArrayList<Boolean>>
    private var isFirstSubmit = true
    private var deleteIconsHidden: Boolean = true

    class ViewHolder(
        view: View, termTextLister: TermEditTextListener, defTextListener: DefinitionEditTextListener,
        private val deleteListener: IconDeleteClickListener?
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
        val imgDelete: FrameLayout = view.findViewById<FrameLayout?>(R.id.imgDeleteTerm).also {
            it.setOnClickListener {
                deleteListener?.onDeleteIconClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(inflater.inflate(
            R.layout.terms_edit_list_item,
            parent,
            false), TermEditTextListener(currentList, errPositions), DefinitionEditTextListener(currentList, errPositions), deleteListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val term = currentList[position]

        holder.termListener.apply {
            this.updateBindings(holder)
            this.disableListener(true)
        }
        holder.defListener.apply {
            this.updateBindings(holder)
            this.disableListener(true)
        }

        if (currentList.size > 2)
            holder.imgDelete.visibility = View.VISIBLE
        else
            holder.imgDelete.visibility = View.INVISIBLE

        holder.term.setText(term.term)
        holder.definition.setText(term.definition)

        // Enabling listeners after calling setText() method
        holder.termListener.disableListener(false)
        holder.defListener.disableListener(false)

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

    override fun getItemCount() = currentList.size


    class TermEditTextListener(
        private val terms: MutableList<Entities.Term>,
        private val errPositions: MutableList<ArrayList<Boolean>>
    ) : TextWatcher {

        private var position = 0
        private lateinit var container: TextInputLayout
        private var isDisabled = false

        fun updateBindings(holder: ViewHolder)
        {
            this.position = holder.adapterPosition
            container = holder.editTermContainer
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {
            if (!isDisabled)
            {
                terms[position].term = p0.toString()
                if (container.error != "" && terms[position].term.isNotBlank())
                {
                    container.error = ""
                    errPositions[position][0] = false // now this field is not empty so no errors
                }
            }
        }
        override fun afterTextChanged(p0: Editable?) { }

        fun disableListener(disable: Boolean)
        {
            isDisabled = disable
        }
    }

    class DefinitionEditTextListener(
        private val terms: MutableList<Entities.Term>,
        private val errPositions: MutableList<ArrayList<Boolean>>
    ) : TextWatcher {

        private var position = 0
        private lateinit var container: TextInputLayout
        private var isDisabled = false

        fun updateBindings(holder: ViewHolder)
        {
            this.position = holder.adapterPosition
            container = holder.editDefinitionContainer
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {
            if (!isDisabled)
            {
                terms[position].definition = p0.toString()
                if (container.error != "" && terms[position].definition.isNotBlank())
                {
                    container.error = ""
                    errPositions[position][1] = false // now this field is not empty so no errors
                }
            }
        }
        override fun afterTextChanged(p0: Editable?) {}

        fun disableListener(disable: Boolean)
        {
            isDisabled = disable
        }
    }

    // if new term added increase size of array for tracking input
    fun notifyNewTermInserted(position: Int)
    {
        errPositions.add(arrayListOf(false, false))
        this.notifyItemInserted(position)
    }

    fun notifyTermRemoved(position: Int)
    {
        errPositions.removeAt(position)
        this.notifyItemRemoved(position)
    }

    override fun submitList(list: MutableList<Entities.Term>?) {
        super.submitList(list)

        if (list != null && isFirstSubmit) {
            errPositions  = MutableList(list.size) {
                arrayListOf(false, false)
            }
        }
        isFirstSubmit = false
    }

    fun checkHideShowDeleteIcons(view: RecyclerView)
    {
        if (currentList.size < 3)
        {
            view.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<FrameLayout>(R.id.imgDeleteTerm)?.visibility = View.INVISIBLE
            view.findViewHolderForAdapterPosition(1)?.itemView?.findViewById<FrameLayout>(R.id.imgDeleteTerm)?.visibility = View.INVISIBLE
            deleteIconsHidden = true
        }
        else if (deleteIconsHidden) {
            view.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<FrameLayout>(R.id.imgDeleteTerm)?.visibility = View.VISIBLE
            view.findViewHolderForAdapterPosition(1)?.itemView?.findViewById<FrameLayout>(R.id.imgDeleteTerm)?.visibility = View.VISIBLE
            deleteIconsHidden = false
        }
    }

    fun setOnDeleteIconClickListener(listener: IconDeleteClickListener)
    {
        deleteListener = listener
    }
}