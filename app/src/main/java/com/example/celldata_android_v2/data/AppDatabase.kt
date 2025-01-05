package com.example.celldata_android_v2.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CellInfoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cellInfoDao(): CellInfoDao
}