package com.opsu.thesaurus.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.adapters.SetsAdapter
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.database.viewmodels.SetViewModel

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
        setsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Observe for a new data from database
        mSetViewModel = ViewModelProvider(this)[SetViewModel::class.java]
        mSetViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        return view
    }

    fun addNewSet(set: Entities.Set) {
        mSetViewModel.addSet(set) // adding item to database
    }

}