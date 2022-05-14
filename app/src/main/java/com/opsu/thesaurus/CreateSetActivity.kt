package com.opsu.thesaurus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsu.thesaurus.adapters.TermsEditAdapter
import com.opsu.thesaurus.databinding.ActivityCreateSetBinding
import com.opsu.thesaurus.models.DataModels
import java.io.Serializable

class CreateSetActivity : AppCompatActivity() {
    val DEFAULT_USER_NAME = "You"

    private lateinit var binding: ActivityCreateSetBinding
    private val dataList: MutableList<DataModels.TermModel> = mutableListOf(
        DataModels.TermModel("", ""),
        DataModels.TermModel("", "")
    )
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSetBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = TermsEditAdapter(layoutInflater, dataList)
        val termsList: RecyclerView = binding.termsList
        termsList.adapter = adapter
        termsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.txtTitle.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                title = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) { }
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
        for (term in dataList)
            // TODO: Implement error messages to user telling what's wrong
            if (term.term.isBlank() || term.definition.isBlank() || title.isBlank()) return
        val intent = Intent()
        val data = DataModels.SetModel(title, dataList.size, DEFAULT_USER_NAME, ArrayList(dataList))

        intent.putExtra("NewSet", data as Serializable)
        setResult(RESULT_OK, intent)
        finish()
    }

}