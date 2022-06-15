package com.opsu.thesaurus.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.opsu.thesaurus.R

class GameWrongAnswerDialog(
    private val term: String,
    private val correct: String,
    private val wrong: String) : DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        return activity?.let { it ->
            val builder = MaterialAlertDialogBuilder(it, R.style.GameWrongAnswerAlertDialogTheme)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_game_wrong_answer, null)

            view.findViewById<TextView>(R.id.txtTerm).text = term
            view.findViewById<TextView>(R.id.txtCorrectAnswer).text = correct
            view.findViewById<TextView>(R.id.txtWrongAnswer).text = wrong

            view.findViewById<MaterialButton>(R.id.btnContinue).setOnClickListener {
                setFragmentResult("requestContinue", bundleOf())
                dismiss()
            }

            builder.setView(view)

            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}