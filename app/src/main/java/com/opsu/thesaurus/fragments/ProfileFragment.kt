package com.opsu.thesaurus.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.opsu.thesaurus.R
import com.opsu.thesaurus.fragments.dialogs.InputNameDialog

class ProfileFragment : Fragment()
{
    private lateinit var txtProfileName: TextView
    private lateinit var defNameText: String
    private var userName: String? = null
    private val nameKey = "name"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val nameField = view.findViewById<LinearLayout>(R.id.nameField)
        nameField.setOnClickListener {
            val dialog = InputNameDialog(userName)
            dialog.show(childFragmentManager, "inputNameDialog")
        }
        txtProfileName = nameField.findViewById(R.id.txtProfileName)

        defNameText = resources.getString(R.string.user_name_placeholder)

        userName = requireActivity().getSharedPreferences("UserName", Context.MODE_PRIVATE).getString(nameKey, null)
        txtProfileName.text = userName ?: defNameText


        childFragmentManager.setFragmentResultListener("getName", viewLifecycleOwner) { _, result ->
            val name = result.getString("name")
            if (name != null)
            {
                txtProfileName.text = name.toString()
                saveName(name.toString())
            }
        }

        return view
    }

    private fun saveName(name: String)
    {
        requireActivity().getSharedPreferences("UserName", Context.MODE_PRIVATE).edit().also {
            it.putString(nameKey, name)
            it.apply()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

}