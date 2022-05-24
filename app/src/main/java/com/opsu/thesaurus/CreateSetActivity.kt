package com.opsu.thesaurus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.opsu.thesaurus.adapters.TermsEditAdapter
import com.opsu.thesaurus.databinding.ActivityCreateSetBinding
import com.opsu.thesaurus.database.entities.Entities
import java.io.Serializable

class CreateSetActivity : AppCompatActivity() {
    private val DEFAULT_USER_NAME = "You"

    private lateinit var termsList: RecyclerView
    private lateinit var adapter: TermsEditAdapter
    private lateinit var binding: ActivityCreateSetBinding

    private val dataList: MutableList<Entities.Term> = mutableListOf(
        Entities.Term(0, "", ""),
        Entities.Term(0, "", "")
    )
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSetBinding.inflate(layoutInflater)

        setContentView(binding.root)

        adapter = TermsEditAdapter(layoutInflater, dataList)
        termsList = binding.termsList
        termsList.adapter = adapter
        termsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.txtTitle.addTextChangedListener(object : TextWatcher{
            val container = binding.txtTitleContainer
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                title = p0.toString()
                if (!container.error.isNullOrBlank())
                    container.error = ""
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.toolbar.toolbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.toolbar.toolbarDone.setOnClickListener {
            formResult()
        }

        binding.fabAdd.setOnClickListener {
            dataList.add(Entities.Term(0, "", ""))
            adapter.notifyNewTermInserted(dataList.size)
        }

    }

    private fun formResult()
    {
        val isCorrect = checkInput(dataList)
        if (!isCorrect) return

        val intent = Intent()
        val set = Entities.Set(title, dataList.size, DEFAULT_USER_NAME)

        intent.putExtra("set", set as Serializable)
        intent.putExtra("terms", dataList as Serializable)

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun checkInput(terms: MutableList<Entities.Term>): Boolean
    {
        var isCorrect = true

        if (title.isBlank())
        {
            binding.txtTitleContainer.error = TermsEditAdapter.TITLE_NOT_SET
            isCorrect = false
        }

        for ((i, term) in terms.withIndex())
        {
            val vh = termsList.findViewHolderForAdapterPosition(i)

            if (vh != null)
            {
                if (term.term.isBlank())
                {
                    vh.itemView.findViewById<TextInputLayout>(R.id.editTermContainer)
                        .error = TermsEditAdapter.FIELD_REQUIRED
                    adapter.errPositions[i][0] = true // this item has an error
                    isCorrect = false
                }
                if (term.definition.isBlank())
                {
                    vh.itemView.findViewById<TextInputLayout>(R.id.editDefinitionContainer)
                        .error = TermsEditAdapter.FIELD_REQUIRED
                    adapter.errPositions[i][1] = true // this item has an error
                    isCorrect = false
                }
            }
            else
            {
                if (term.term.isBlank())
                    adapter.errPositions[i][0] = true
                if (term.definition.isBlank())
                    adapter.errPositions[i][1] = true
                adapter.notifyItemChanged(i)
            }
        }
        if (!isCorrect)
            Toast.makeText(this, "You must fill in all fields!", Toast.LENGTH_SHORT).show()

        return isCorrect
    }

}