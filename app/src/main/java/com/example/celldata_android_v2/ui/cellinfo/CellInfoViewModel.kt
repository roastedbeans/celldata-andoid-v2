package com.example.celldata_android_v2.ui.cellinfo


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.mroczis.netmonster.core.model.cell.ICell

class CellInfoViewModel : ViewModel() {
    private val _cellData = MutableLiveData<ICell?>()
    val cellData: LiveData<ICell?> = _cellData


    fun updateCellData(cell: ICell) {
        _cellData.value = cell
    }
}