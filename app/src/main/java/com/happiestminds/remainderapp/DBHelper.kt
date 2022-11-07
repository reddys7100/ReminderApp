package com.happiestminds.remainderapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
    SQLiteOpenHelper(context,"reminder.db", null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        // table creation to be done (executed when db not present)
        db?.execSQL(TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // executed when version mismatch
        // drop table, create new table, modify schema of existing tables
    }

    companion object {
        const val TABLE_NAME = "Reminders"
        const val CLM_TITLE = "title"
        const val CLM_DESCRIPTION = "description"
        const val CLM_DATE = "date"
        const val CLM_TIME = "time"
        private const val TABLE_QUERY = "create table $TABLE_NAME($CLM_TITLE text, " +
                "$CLM_DESCRIPTION text, $CLM_DATE text, $CLM_TIME text)"
    }

}