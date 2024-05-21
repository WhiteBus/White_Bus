package com.google.mediapipe.examples.facelandmarker.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.google.mediapipe.examples.facelandmarker.database.DestinationContract
import com.google.mediapipe.examples.facelandmarker.database.DestinationDbHelper

class DestinationRepository(context: Context) {

    private val dbHelper = DestinationDbHelper(context)
    private val db = dbHelper.writableDatabase

    fun insertDestination(name: String, xCoord: Double, yCoord: Double, busStopNumber: Int): Long {
        val values = ContentValues().apply {
            put(DestinationContract.DestinationEntry.COLUMN_NAME_NAME, name)
            put(DestinationContract.DestinationEntry.COLUMN_NAME_X_COORD, xCoord)
            put(DestinationContract.DestinationEntry.COLUMN_NAME_Y_COORD, yCoord)
            put(DestinationContract.DestinationEntry.COLUMN_NAME_BUS_STOP_NUMBER, busStopNumber)
        }
        return db.insert(DestinationContract.DestinationEntry.TABLE_NAME, null, values)
    }

    fun getAllDestinations(): Cursor {
        return db.query(
            DestinationContract.DestinationEntry.TABLE_NAME,
            null, null, null, null, null, null
        )
    }

    fun close() {
        dbHelper.close()
    }
}
