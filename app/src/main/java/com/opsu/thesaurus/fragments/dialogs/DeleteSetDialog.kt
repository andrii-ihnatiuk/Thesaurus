package com.opsu.thesaurus.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.opsu.thesaurus.R

class DeleteSetDialog : DialogFragment()
{
    private lateinit var listener: DeleteSetDialogListener

    interface DeleteSetDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DeleteSetDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() +
                    " must implement DeleteSetDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it, R.style.AlertDialogTheme)
            builder
                .setTitle("Delete set")
                .setMessage("This operation can't be undone. Are you sure?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    listener.onDialogPositiveClick(this)
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dismiss()
                })
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}