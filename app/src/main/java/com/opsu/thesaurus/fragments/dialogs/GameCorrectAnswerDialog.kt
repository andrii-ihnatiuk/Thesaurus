package com.opsu.thesaurus.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.opsu.thesaurus.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameCorrectAnswerDialog : DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        return activity?.let { it ->
            val builder = MaterialAlertDialogBuilder(it, R.style.GameCorrectAnswerAlertDialogTheme)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_game_correct_answer, null)

            builder.setView(view)
            val dialog = builder.create()
            dialog.setOnShowListener {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    dismiss()
                }
            }

            dialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface)
    {
        super.onDismiss(dialog)
        setFragmentResult("requestContinue", bundleOf())
    }
}