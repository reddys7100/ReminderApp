package com.happiestminds.remainderapp

import DBWrapper
import Reminder
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.NonCancellable.cancel

class DialogBox: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dlg: Dialog? = null
        // val message = arguments?.getString("msg")
        val title = arguments?.getString("title")
        val descr = arguments?.getString("description")
        val date = arguments?.getString("date")
        val time = arguments?.getString("time")

        val rem = Reminder(title!!,descr!!,date!!,time!!)

        //create a dialog here
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Title: $title")
            builder.setMessage("Description: $descr\nDate: $date\nTime: $time")
            builder.setPositiveButton("Edit"){ dialog, i ->                // executed button clicking
                dialog.cancel()
            }
            builder.setNegativeButton("Delete") { dialog, i ->
                DBWrapper(it).deleteReminder(rem)
                activity?.finish()
                dialog.cancel()
                val intent= Intent(requireContext(),MainActivity()::class.java)
                startActivity(intent)
            }
            builder.setNeutralButton("Cancel") { dialog, i ->
                dialog.cancel()
            }
            dlg = builder.create()
        }
        return dlg!!
    }
}
