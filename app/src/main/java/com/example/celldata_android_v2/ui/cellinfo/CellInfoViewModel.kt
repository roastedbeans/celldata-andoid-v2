package com.example.celldata_android_v2.ui.cellinfo

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.mroczis.netmonster.core.factory.NetMonsterFactory
import cz.mroczis.netmonster.core.model.cell.ICell

class CellInfoViewModel(application: Application) : AndroidViewModel(application) {
    private val _cellData = MutableLiveData<List<ICell>>()
    val cellData: LiveData<List<ICell>> = _cellData

    private val handler = Handler(Looper.getMainLooper())
    private val netMonster = NetMonsterFactory.get(getApplication())

    private var isUpdating = false

    private fun hasRequiredPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    getApplication(),
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun startUpdates() {
        if (!isUpdating) {
            isUpdating = true
            updateData()
        }
    }

    fun stopUpdates() {
        isUpdating = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateData() {
        if (!isUpdating) return

        if (hasRequiredPermissions()) {
            try {
                val cells = netMonster.getCells()
                _cellData.postValue(cells)
                Log.d("CellInfo", "Updated cells: ${cells.size}")
            } catch (securityException: SecurityException) {
                Log.e("CellInfo", "Security exception when accessing cell data", securityException)
                stopUpdates()
            } catch (e: Exception) {
                Log.e("CellInfo", "Error updating cells", e)
            }

            handler.postDelayed({ updateData() }, REFRESH_INTERVAL)
        } else {
            Log.w("CellInfo", "Required permissions not granted")
            stopUpdates()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdates()
    }

    companion object {
        private const val REFRESH_INTERVAL = 5000L
    }
}