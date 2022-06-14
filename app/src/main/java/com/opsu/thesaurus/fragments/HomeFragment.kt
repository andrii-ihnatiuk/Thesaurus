package com.opsu.thesaurus.fragments

import android.content.Intent
import android.os.Bundle
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
import com.opsu.thesaurus.database.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var adapter: SetsAdapter
    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var setsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        adapter = SetsAdapter(layoutInflater)
        setsList = view.findViewById(R.id.setsList)
        setsList.adapter = adapter
        adapter.setOnItemClickLister(object : SetsAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), ViewSetActivity::class.java)
                val clickedSet = adapter.currentList[position]

                intent.putExtra("setId", clickedSet.setId)
                intent.putExtra("userName", clickedSet.createdBy)
                startActivity(intent)
            }
        })
        setsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Observe for a new data from database
        mHomeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        mHomeViewModel.readAllSets.observe(viewLifecycleOwner) {
            val oldSize = adapter.currentList.size
            adapter.submitList(it)
            setsList.post {
                if (oldSize < it.size)
                    setsList.smoothScrollToPosition(0)
            }
        }
        return view
    }

}