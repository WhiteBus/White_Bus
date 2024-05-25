package com.google.mediapipe.examples.facelandmarker.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.google.mediapipe.examples.facelandmarker.database.DestinationContract
import com.google.mediapipe.examples.facelandmarker.database.DestinationDbHelper

class DestinationRepository(context: Context) {

    private val dbHelper = DestinationDbHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    fun insertDestination(name: String, xCoord: Double, yCoord: Double, address: String): Long {
        val values = ContentValues().apply {
            put(DestinationContract.DestinationEntry.COLUMN_NAME_NAME, name)
            put(DestinationContract.DestinationEntry.COLUMN_NAME_X_COORD, xCoord)
            put(DestinationContract.DestinationEntry.COLUMN_NAME_Y_COORD, yCoord)
            put(DestinationContract.DestinationEntry.COLUMN_NAME_ADDRESS, address)
        }
        return db.insert(DestinationContract.DestinationEntry.TABLE_NAME, null, values)
    }

    fun getAllDestinations(): Cursor {
        return db.query(
            DestinationContract.DestinationEntry.TABLE_NAME,
            null, null, null, null, null, null
        )
    }

    fun getDestinationCoordinatesByName(name: String): Pair<Double, Double>? {
        val cursor = db.query(
            DestinationContract.DestinationEntry.TABLE_NAME,
            arrayOf(
                DestinationContract.DestinationEntry.COLUMN_NAME_X_COORD,
                DestinationContract.DestinationEntry.COLUMN_NAME_Y_COORD
            ),
            "${DestinationContract.DestinationEntry.COLUMN_NAME_NAME} = ?",
            arrayOf(name),
            null, null, null
        )

        var coordinates: Pair<Double, Double>? = null

        with(cursor) {
            if (moveToFirst()) {
                val xCoord = getDouble(getColumnIndexOrThrow(DestinationContract.DestinationEntry.COLUMN_NAME_X_COORD))
                val yCoord = getDouble(getColumnIndexOrThrow(DestinationContract.DestinationEntry.COLUMN_NAME_Y_COORD))
                coordinates = Pair(xCoord, yCoord)
            }
            close()
        }

        return coordinates
    }

    fun close() {
        dbHelper.close()
    }
}
