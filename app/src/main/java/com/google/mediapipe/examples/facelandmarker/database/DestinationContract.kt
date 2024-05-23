package com.google.mediapipe.examples.facelandmarker.database

import android.provider.BaseColumns

object DestinationContract {
    object DestinationEntry : BaseColumns {
        const val TABLE_NAME = "destinations"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_X_COORD = "x_coord"
        const val COLUMN_NAME_Y_COORD = "y_coord"
        const val COLUMN_NAME_ADDRESS = "address"
    }
}
