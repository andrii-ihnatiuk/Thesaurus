package com.opsu.thesaurus.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.adapters.SetsAdapter
import com.opsu.thesaurus.models.DataModels

class HomeFragment : Fragment() {

    private lateinit var adapter: SetsAdapter

    private val list : MutableList<DataModels.SetModel> = mutableListOf(
        DataModels.SetModel("English", 2, "Andrew", ArrayList()),
        DataModels.SetModel("German", 3, "Daniil", ArrayList())
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        adapter = SetsAdapter(layoutInflater, list)
        val setsList: RecyclerView = view.findViewById(R.id.setsList)
        setsList.adapter = adapter
        setsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    fun addNewSet(set: DataModels.SetModel) {
        list.add(set)
        adapter.notifyItemInserted(list.size - 1)
    }

}