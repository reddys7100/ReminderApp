

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.happiestminds.remainderapp.DBHelper

class DBWrapper(ctx: Context) {

    val helper = DBHelper(ctx) // tables are ready
    val db = helper.writableDatabase

    fun addReminder(rmd: Reminder): Boolean{
        //insert
        val values = ContentValues()
        values.put(DBHelper.CLM_TITLE, rmd.title)
        values.put(DBHelper.CLM_DESCRIPTION,rmd.description)
        values.put(DBHelper.CLM_DATE, rmd.date)
        values.put(DBHelper.CLM_TIME, rmd.time)

        val rowid = db.insert(DBHelper.TABLE_NAME, null, values)
        if (rowid.toInt() == -1){
            return false
        }
        return true
    }

    fun getAllReminder(): Cursor {
        //query
        val clms = arrayOf(DBHelper.CLM_TITLE, DBHelper.CLM_DESCRIPTION, DBHelper.CLM_DATE, DBHelper.CLM_TIME)

        return db.query(DBHelper.TABLE_NAME, clms,
            null, null, null, null, null)
    }

    fun deleteReminder(rmd: Reminder){
        //delete
        db.delete(DBHelper.TABLE_NAME, "${DBHelper.CLM_TITLE} = ?",
            arrayOf(rmd.title.toString()))
    }
}

data class Reminder(val title: String, val description: String, val date: String, val time: String){
    override  fun toString(): String {
        return """
            Title: $title
            Description: $description
            Date: $date
            Time: $time
        """.trimIndent()
    }
}