package com.opsu.thesaurus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.opsu.thesaurus.adapters.TermsEditAdapter
import com.opsu.thesaurus.databinding.ActivityManageSetBinding
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.database.viewmodels.ManageSetViewModel
import com.opsu.thesaurus.database.viewmodels.ManageSetViewModelFactory
import kotlinx.coroutines.*

class ManageSetActivity : AppCompatActivity() {
    private var isCalledToEditSet = false

    private lateinit var termsList: RecyclerView
    private lateinit var adapter: TermsEditAdapter
    private lateinit var binding: ActivityManageSetBinding
    private lateinit var mManageSetViewModel: ManageSetViewModel

    private var loadedSet: Entities.Set = Entities.Set("", 0, "")

    private var dataList: MutableList<Entities.Term> = mutableListOf(
        Entities.Term("", ""),
        Entities.Term( "", "")
    )
    private lateinit var initialDataList: List<Entities.Term>

    private var title: String = ""
    private val nameKey = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageSetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TermsEditAdapter(layoutInflater)

        if (callingActivity?.className == ViewSetActivity::class.java.name)
        {
            isCalledToEditSet = true
            val setId = intent.getLongExtra("setId", -1)
            mManageSetViewModel = ViewModelProvider(this, ManageSetViewModelFactory(application, setId))[ManageSetViewModel::class.java]
            mManageSetViewModel.getSetById()?.observe(this) {
                loadedSet = it
                title = it.setTitle
                binding.txtTitle.setText(title)
            }
            mManageSetViewModel.getTermsBySet()?.observe(this) {
                dataList = it.toMutableList()
                initialDataList = it.map { el -> el.copy() }
                adapter.submitList(dataList)
            }
            binding.toolbar.txtToolbarTitle.text = resources.getString(R.string.edit_set_title)
        }
        else {
            mManageSetViewModel = ViewModelProvider(this, ManageSetViewModelFactory(application, null))[ManageSetViewModel::class.java]
            adapter.submitList(dataList)
        }

        adapter.setOnDeleteIconClickListener(object : TermsEditAdapter.IconDeleteClickListener {
            override fun onDeleteIconClick(position: Int)
            {
                dataList.removeAt(position)
                adapter.notifyTermRemoved(position)
                adapter.checkHideShowDeleteIcons(termsList)
            }
        })
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
            override fun afterTextChanged(p0: Editable?) { }
        })

        binding.toolbar.toolbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.toolbar.toolbarDone.setOnClickListener {
            updateDb()
        }

        binding.fabAdd.setOnClickListener {
            dataList.add(Entities.Term("", ""))
            adapter.notifyNewTermInserted(dataList.size)
            termsList.post {
                termsList.smoothScrollToPosition(dataList.size - 1)
            }
            adapter.checkHideShowDeleteIcons(termsList)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun updateDb()
    {
        val isCorrect = checkInput(dataList)
        if (!isCorrect) return

        if (isCalledToEditSet)
        {
            loadedSet.numOfTerms = dataList.size
            val (updateList, deleteList) = findListDiff(initialDataList, dataList)
            val insertList: List<Entities.Term> = dataList.filter {
                it.termId == null
            }
            if (title.trim() != loadedSet.setTitle)
                mManageSetViewModel.updateSetTitle(loadedSet.setId, title)

            runBlocking {
                launch(Dispatchers.Default)
                {
                    val tasks = mutableListOf<Deferred<Any>>()
                    if (updateList.isNotEmpty())
                        tasks.add(
                            async { mManageSetViewModel.updateTerms(updateList) }
                        )
                    if (deleteList.isNotEmpty())
                        tasks.add(
                            async { mManageSetViewModel.deleteTermsFromSet(deleteList, loadedSet) }
                        )
                    if (insertList.isNotEmpty())
                        tasks.add(
                            async { mManageSetViewModel.addTermsToSet(insertList, loadedSet) }
                        )
                    tasks.awaitAll()
                }.invokeOnCompletion {
                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
        else {
            var count = 0
            runBlocking {
                val task = async(Dispatchers.Default) { mManageSetViewModel.checkTitleIsTaken(title.trim()) }
                count = task.await()
            }
            if (count == 1)
            {
                binding.txtTitleContainer.error = "This title is already taken"
                return
            }

            val userName: String? = getSharedPreferences("UserName", MODE_PRIVATE).getString(nameKey, null)

            val DEFAULT_USER_NAME = resources.getString(R.string.def_username)
            val set = Entities.Set(title.trim(), dataList.size, userName ?: DEFAULT_USER_NAME)
            mManageSetViewModel.addSetWithTerms(set, dataList).invokeOnCompletion {
                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()
            }
        }
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

    private fun findListDiff(initial: List<Entities.Term>, modified: MutableList<Entities.Term>): Array<MutableList<Entities.Term>>
    {
        val toUpdate: MutableList<Entities.Term> = mutableListOf()
        val toDelete: MutableList<Entities.Term> = mutableListOf()

        initial.forEach {
            val term: Entities.Term? = modified.find { el -> el.termId == it.termId }
            // found element
            if (term != null)
            {
                if (term != it)
                {
                    toUpdate.add(term)
                }
            }
            // not found in new list (term is deleted)
            else {
                toDelete.add(it)
            }
        }

        return arrayOf(toUpdate, toDelete)
    }

}