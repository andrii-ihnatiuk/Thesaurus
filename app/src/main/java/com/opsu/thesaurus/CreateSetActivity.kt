package com.opsu.thesaurus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.opsu.thesaurus.adapters.TermsEditAdapter
import com.opsu.thesaurus.databinding.ActivityCreateSetBinding
import com.opsu.thesaurus.models.DataModels
import java.io.Serializable

class CreateSetActivity : AppCompatActivity() {
    val DEFAULT_USER_NAME = "You"
    val FIELD_REQUIRED = "This field is required"
    val TITLE_NOT_SET = "Must specify title"
    private lateinit var termsList: RecyclerView
    private lateinit var binding: ActivityCreateSetBinding

    private val dataList: MutableList<DataModels.TermModel> = mutableListOf(
        DataModels.TermModel("", ""),
        DataModels.TermModel("", "")
    )
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSetBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = TermsEditAdapter(layoutInflater, dataList)
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

    }

    private fun formResult()
    {
        val isCorrect = checkInput(dataList)
        if (!isCorrect) return

        val intent = Intent()
        val data = DataModels.SetModel(title, dataList.size, DEFAULT_USER_NAME, ArrayList(dataList))

        intent.putExtra("NewSet", data as Serializable)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun checkInput(terms: MutableList<DataModels.TermModel>): Boolean
    {
        var isCorrect = true

        if (title.isBlank())
        {
            binding.txtTitleContainer.error = TITLE_NOT_SET
            isCorrect = false
        }

        for ((i, term) in terms.withIndex())
        {
            Log.d("pos", i.toString())
            val vh = termsList.findViewHolderForAdapterPosition(i)

            if (vh != null)
            {
                if (term.term.isBlank())
                {
                    vh.itemView.findViewById<TextInputLayout>(R.id.editTermContainer)
                        .error = FIELD_REQUIRED
                    isCorrect = false
                }
                if (term.definition.isBlank())
                {
                    vh.itemView.findViewById<TextInputLayout>(R.id.editDefinitionContainer)
                        .error = FIELD_REQUIRED
                    isCorrect = false
                }
            }
        }
        if (!isCorrect)
            Toast.makeText(this, "At least 2 terms are required", Toast.LENGTH_SHORT).show()

        return isCorrect
    }

}