package com.example.celldata_android_v2

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.celldata_android_v2.ui.cellinfo.CellInfoViewModel
import com.example.celldata_android_v2.ui.cellinfo.CellInfoFragment
import kotlinx.coroutines.*

class BackgroundService : Service() {
    private var cellInfoFragment: CellInfoFragment? = null
    private lateinit var cellInfoViewModel: CellInfoViewModel
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        cellInfoViewModel = CellInfoViewModel(application)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_SERVICE -> startDataCollection()
            ACTION_STOP_SERVICE -> stopDataCollection()
        }
        // If system kills the service, restart it
        return START_STICKY
    }


    private fun startDataCollection() {
        if (!isRunning) {
            isRunning = true
            serviceScope.launch {
                Log.d("BackgroundService", "Collecting cellular data ${cellInfoViewModel}")
                while (isRunning && ::cellInfoViewModel.isInitialized) {
                    cellInfoViewModel.updateData()
                }
            }
        }
    }

    private fun stopDataCollection() {
        isRunning = false
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cell Data Collection",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Background cellular data collection service"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("NetSense")
            .setContentText("Collecting cellular data in background")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        isRunning = false
    }

    companion object {
        private const val TAG = "BackgroundService"
        private const val CHANNEL_ID = "cell_data_service_channel"
        private const val NOTIFICATION_ID = 1
        private const val REFRESH_INTERVAL = 5000L
        private const val ERROR_RETRY_INTERVAL = 10000L
        const val ACTION_START_SERVICE = "START_SERVICE"
        const val ACTION_STOP_SERVICE = "STOP_SERVICE"
    }
}