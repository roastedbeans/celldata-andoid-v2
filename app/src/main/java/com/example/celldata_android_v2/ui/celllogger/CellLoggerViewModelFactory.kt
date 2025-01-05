package com.example.celldata_android_v2.ui.celllogger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.celldata_android_v2.data.CellInfoDao

class CellLoggerViewModelFactory(private val cellInfoDao: CellInfoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CellLoggerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CellLoggerViewModel(cellInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
