package com.happiestminds.remainderapp

import DBWrapper
import Reminder
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    lateinit var titleEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var dateTextView: TextView
    lateinit var timeTextView: TextView
    lateinit var dateButton: Button
    lateinit var timeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        titleEditText = findViewById(R.id.titleT)
        descriptionEditText = findViewById(R.id.descriptionT)
        dateTextView = findViewById(R.id.dateT)
        timeTextView = findViewById(R.id.timeT)
        dateButton = findViewById(R.id.dateB)
        timeButton = findViewById(R.id.timeB)
    }

    fun dateClick(view: View) {
        val dlg = DatePickerDialog(this)
        dlg.setOnDateSetListener { dPicker, year, month, day ->
            dateTextView.text = "$day-${month+1}-$year"
        }
        dlg.show()
    }

    fun timeClick(view: View) {
        val dlg = TimePickerDialog(this, this,
            10, 0, true)
        dlg.show()
    }

    override fun onTimeSet(tPicker: TimePicker?, hh: Int, mm: Int) {
        timeTextView.text = "$hh:$mm"
    }

    fun addClick(view: View) {

        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val date = dateTextView.text.toString()
        val time = timeTextView.text.toString()

        // start
        when {
            title.isEmpty() -> titleEditText.error = "Title is mandatory"
            description.isEmpty() -> descriptionEditText.error = "Description is mandatory"
            date.isEmpty() -> dateTextView.error = "Date is mandatory"
            time.isEmpty() -> timeTextView.error = "Time is mandatory"

            else -> {
                // start
                val dateString="$date $time"
                val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
                val Date = format.parse(dateString)
                val cal = Calendar.getInstance()
                cal.time = Date
                Log.d("Add reminder", "milli: ${cal.timeInMillis}")
                val rmd = Reminder(title, description, date, time)
                if (DBWrapper(this).addReminder(rmd)) {
                    Toast.makeText(this, "Reminder added",
                        Toast.LENGTH_LONG).show()
                    Toast.makeText(this, "Task Scheduled Successfully",
                        Toast.LENGTH_SHORT).show();


                    val value =  ContentValues();

                    value.put(CalendarContract.Events.DTSTART, cal.timeInMillis)
                    value.put(CalendarContract.Events.DTEND, cal.timeInMillis + 60*1000);
                    value.put(CalendarContract.Events.TITLE, title);
                    value.put(CalendarContract.Events.DESCRIPTION, description);
                    value.put(CalendarContract.Events.CALENDAR_ID, 1);
                    value.put(CalendarContract.Events.EVENT_TIMEZONE,"IST")
                    value.put(CalendarContract.Events.HAS_ALARM, 1)

                    val uri1 = contentResolver.insert(CalendarContract.Events.CONTENT_URI, value);
                    Log.d("Add Reminder", "calenderBtnClick:  $uri1")
                    val evenID = uri1?.lastPathSegment?.toInt()


                    val values =  ContentValues();
                    val cr: ContentResolver = contentResolver
                    values.put(CalendarContract.Reminders.EVENT_ID, 5);
                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_DEFAULT);
                    values.put(CalendarContract.Reminders.MINUTES, CalendarContract.Reminders.MINUTES_DEFAULT);
                    val reminderUri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);

                    Log.d("Add reminder", "reminder uri: $reminderUri")

                } else {
                    Toast.makeText(this, "Reminder not added",
                        Toast.LENGTH_LONG).show()
                }
                finish()
            }
        }
    }

    fun cancelClick(view: View) {
        Log.d("AddActivity", "Cancel clicked..")
        titleEditText.setText("")
        descriptionEditText.setText("")
        dateTextView.setText("")
        timeTextView.setText("")
        finish()
    }
}