package com.example.celldata_android_v2.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    // Get a singleton instance of the database
    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cell_info_database" // Name of the database file
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
