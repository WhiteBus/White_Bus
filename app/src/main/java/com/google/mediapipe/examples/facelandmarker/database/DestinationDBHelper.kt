package com.google.mediapipe.examples.facelandmarker.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DestinationDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "Destination.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${DestinationContract.DestinationEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${DestinationContract.DestinationEntry.COLUMN_NAME_NAME} TEXT," +
                    "${DestinationContract.DestinationEntry.COLUMN_NAME_X_COORD} REAL," +
                    "${DestinationContract.DestinationEntry.COLUMN_NAME_Y_COORD} REAL," +
                    "${DestinationContract.DestinationEntry.COLUMN_NAME_ADDRESS} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${DestinationContract.DestinationEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)

    }
}
