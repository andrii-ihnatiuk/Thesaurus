package com.opsu.thesaurus.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.opsu.thesaurus.R

class InputNameDialog(private val username: String?) : DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = MaterialAlertDialogBuilder(it, R.style.AlertDialogTheme)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_input_name, null)
            val container = view.findViewById<TextInputLayout>(R.id.inputNameContainer)
            val textField = container.findViewById<TextInputEditText>(R.id.txtInputName).also { field ->
                field.setText(username ?: "")
                field.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            container.error = ""
                    }
                    override fun afterTextChanged(p0: Editable?) { }
                })
            }

            builder.setView(view)
                .setTitle("Change username")
                .setPositiveButton("Apply") { _, _ -> }
                .setNegativeButton("Cancel") { _, _ ->
                    setFragmentResult("getName", bundleOf())
                }


            // We need to override listener for positive button after dialog is showed
            // otherwise dialog will be automatically cancelled even if input is incorrect
            val dialog = builder.create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    if (!textField.text.isNullOrBlank())
                    {
                        setFragmentResult(
                            "getName",
                            bundleOf("name" to textField.text.toString().trim())
                        )
                        dismiss()
                    } else {
                        container.error = "Name cannot be empty"
                    }
                }
            }
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}