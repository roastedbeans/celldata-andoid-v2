package com.example.celldata_android_v2.ui.celllogger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celldata_android_v2.data.CellInfoDao
import com.example.celldata_android_v2.data.CellInfoEntity
import kotlinx.coroutines.launch

class CellLoggerViewModel(private val cellInfoDao: CellInfoDao) : ViewModel() {

    // LiveData to hold the cell info data
    private val _cellInfoList = MutableLiveData<List<CellInfoEntity>>()
    val cellInfoList: LiveData<List<CellInfoEntity>> = _cellInfoList

    // Fetch data from the database
    fun loadCellInfo() {
        viewModelScope.launch {
            val cellData = cellInfoDao.getAllCellInfo() // DAO method to get all CellInfoEntity entries
            _cellInfoList.postValue(cellData)  // Update LiveData with fetched data
        }
    }
}
