package com.opsu.thesaurus.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.opsu.thesaurus.R

class GameResultDialog(private val correct: Int, private val total: Int) : DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        return activity?.let { it ->
            val builder = MaterialAlertDialogBuilder(it, R.style.AlertDialogTheme)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_game_result, null)

            view.findViewById<MaterialButton>(R.id.btnFinish).setOnClickListener {
                setFragmentResult("gameFinish", bundleOf("continue" to false))
                dismiss()
            }
            view.findViewById<MaterialButton>(R.id.btnLearnAgain).setOnClickListener {
                setFragmentResult("gameFinish", bundleOf("continue" to true))
                dismiss()
            }
            val txtCorrectCount = view.findViewById<TextView>(R.id.txtCorrectCount)
            txtCorrectCount.text = getString(com.opsu.thesaurus.R.string.game_result_info, correct, total)

            if (correct.toFloat() / total < 0.5)
            {
                view.findViewById<ImageView>(R.id.imgGameResult).setImageResource(R.drawable.ic_confused)
                view.findViewById<TextView>(R.id.txtGameResultTitle).text = getString(R.string.game_result_title_bad)
                txtCorrectCount.setTextColor(ContextCompat.getColor(requireContext(), R.color.incorrect_answer))
            }

            builder.setView(view)

            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}