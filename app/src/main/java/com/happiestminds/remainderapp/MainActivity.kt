package com.happiestminds.remainderapp

import DBWrapper
import Reminder
import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.happiestminds.remainderapp.*

class MainActivity : AppCompatActivity() {

    val reminderList = mutableListOf<Reminder>()
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listView: ListView
    var titleList= mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.reminderL)
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,titleList)
        listView.adapter = adapter

        // Dialog box start
        listView.setOnItemClickListener { adapterView, view, index, l ->
            // continue
            val selectedList = reminderList[index]
            //listRemainder.removeAt(index)
            val dlg = DialogBox()
            val dataBundle = Bundle()
            dlg.isCancelable = false
            dataBundle.putString("msg","Do you want to delete")
            dataBundle.putString("title", selectedList.title)
            dataBundle.putString("description", selectedList.description)
            dataBundle.putString("date", selectedList.date)
            dataBundle.putString("time", selectedList.time)

            dlg.arguments = dataBundle

            dlg.show(supportFragmentManager, null)
            reminderList.removeAt(index)
        }
        // Dialog box end
    }

    override fun onResume() {
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR),1)
        }
        super.onResume()
        setupData()
    }

    private fun setupData() {
        val cursor = DBWrapper(this).getAllReminder()
        if (cursor.count > 0) {
            val idx_title = cursor.getColumnIndexOrThrow(DBHelper.CLM_TITLE)
            val idx_description = cursor.getColumnIndexOrThrow(DBHelper.CLM_DESCRIPTION)
            val idx_date = cursor.getColumnIndexOrThrow(DBHelper.CLM_DATE)
            val idx_time = cursor.getColumnIndexOrThrow(DBHelper.CLM_TIME)
            cursor.moveToFirst()
            reminderList.clear()
            titleList.clear()
            do {
                val title = cursor.getString(idx_title)
                val description = cursor.getString(idx_description)
                val date = cursor.getString(idx_date)
                val time = cursor.getString(idx_time)
                titleList.add(title)
                val rmd = Reminder(title, description, date, time)
                reminderList.add(rmd)
            } while (cursor.moveToNext())

            adapter.notifyDataSetChanged()

            Log.d("ReminderListActivity", "List: $reminderList")
            Toast.makeText(this, "Found:${reminderList.count()}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun buttonClick(view: View) {
        val addIntent = Intent(this, AddActivity::class.java)
        startActivity(addIntent)
    }

}