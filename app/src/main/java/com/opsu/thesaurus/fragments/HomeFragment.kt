package com.opsu.thesaurus.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.ViewSetActivity
import com.opsu.thesaurus.adapters.SetsAdapter
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.database.entities.Relations
import com.opsu.thesaurus.database.viewmodels.SetViewModel
import java.io.Serializable

class HomeFragment : Fragment() {

    private lateinit var adapter: SetsAdapter
    private lateinit var mSetViewModel: SetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        adapter = SetsAdapter(layoutInflater)
        val setsList: RecyclerView = view.findViewById(R.id.setsList)
        setsList.adapter = adapter
        adapter.setOnItemClickLister(object : SetsAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), ViewSetActivity::class.java)
                val clickedSet = adapter.currentList[position]

                mSetViewModel.getTermsBySet(clickedSet.setTitle).observe(viewLifecycleOwner) {
                    Log.d("terms", it.toString())
                    intent.putExtra("set", clickedSet as Serializable)
                    intent.putExtra("terms", it as Serializable)
                    startActivity(intent)
                }
            }
        })
        setsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Observe for a new data from database
        mSetViewModel = ViewModelProvider(this)[SetViewModel::class.java]
        mSetViewModel.readAllData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return view
    }

    fun addNewSet(set: Entities.Set, terms: List<Entities.Term>) {
        mSetViewModel.addSet(set) // adding item to database
        Log.d("terms", terms.toString())
        mSetViewModel.addTerms(terms).observe(viewLifecycleOwner) { idList ->
            idList.forEach {
                mSetViewModel.addSetTermCrossRef(Relations.SetTermCrossRef(set.setTitle, it.toInt()))
            }
        }
    }

}