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

/**
 * CellInfoViewModel
 *
 * Core data collection component for cellular network analysis. This ViewModel manages
 * real-time collection of cellular network data across different network technologies,
 * focusing on continuous monitoring and data updates for research purposes.
 *
 * Key Features:
 * - Periodic data collection (5-second intervals)
 * - Permission management for secure data access
 * - Error handling and logging
 * - Lifecycle-aware data management
 *
 * Research Applications:
 * - Network behavior monitoring
 * - Signal strength analysis
 * - Cell tower tracking
 * - Connection state analysis
 */
class CellInfoViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * LiveData containing collected cell information.
     * Exposed as immutable LiveData to prevent external modifications.
     *
     * Usage:
     * viewModel.cellData.observe(lifecycleOwner) { cells ->
     *     // Process updated cell data
     * }
     */
    private val _cellData = MutableLiveData<List<ICell>>()
    val cellData: LiveData<List<ICell>> = _cellData

    /**
     * Handler for managing periodic updates on the main thread.
     * Used to schedule regular data collection at REFRESH_INTERVAL intervals.
     */
    private val handler = Handler(Looper.getMainLooper())

    /**
     * NetMonster instance for accessing cellular network information.
     * Provides access to detailed cell information across different technologies.
     */
    private val netMonster = NetMonsterFactory.get(getApplication())

    private var isUpdating = false

    /**
     * Verifies required permissions for data collection.
     *
     * Required Permissions:
     * - ACCESS_COARSE_LOCATION: For cell location data
     * - READ_PHONE_STATE: For detailed cell information
     *
     * @return Boolean indicating if all required permissions are granted
     */
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

    /**
     * Initiates periodic data collection.
     * Starts the update cycle if not already running.
     *
     * Usage:
     * viewModel.startUpdates()
     */
    fun startUpdates() {
        if (!isUpdating) {
            isUpdating = true
            updateData()
        }
    }

    /**
     * Stops the data collection process.
     * Cleans up handler callbacks and stops updates.
     *
     * Usage:
     * viewModel.stopUpdates()
     */
    fun stopUpdates() {
        isUpdating = false
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * Core data collection function.
     * Performs periodic collection of cell information with error handling.
     *
     * Implementation:
     * 1. Checks permissions
     * 2. Collects cell data using NetMonster
     * 3. Updates LiveData with new information
     * 4. Schedules next update
     * 5. Handles potential errors
     *
     * Error Handling:
     * - SecurityException: Permission-related errors
     * - General Exception: Other potential errors
     */
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

     /**
     * Lifecycle cleanup method.
     * Ensures proper cleanup when ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        stopUpdates()
    }

    /**
     * Companion object containing configuration constants.
     * REFRESH_INTERVAL: Time between data collection cycles (5000ms)
     */
    companion object {
        private const val REFRESH_INTERVAL = 5000L
    }
}