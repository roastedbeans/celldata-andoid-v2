package com.example.celldata_android_v2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cell_info")
data class CellInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,      // Auto-generated primary key
    val net: String,                                      // Network type (e.g., LTE, 5G NR)
    val connectionStatus: String,                         // Connection status (e.g., connected, idle)
    val signalStrength: String,                           // General signal strength
    val timestamp: String,                                // Timestamp of the data collection
    val frequency: String,                                // Frequency band (e.g., 1800 MHz)
    val bandWidth: String,                                // Bandwidth (e.g., 20 MHz)
    val mcc: String,                                      // Mobile Country Code
    val mnc: String,                                      // Mobile Network Code
    val iso: String,                                      // ISO country code
    val eci: String,                                      // E-UTRAN Cell Identifier
    val eNb: String,                                      // eNodeB ID
    val cid: String,                                      // Cell ID
    val tac: String,                                      // Tracking Area Code
    val pci: String,                                      // Physical Cell ID
    val rssi: String,                                     // Received Signal Strength Indicator
    val rsrp: String,                                     // Reference Signal Received Power
    val rsrq: String,                                     // Reference Signal Received Quality
    val sinr: String,                                     // Signal-to-Interference-plus-Noise Ratio
    val ssRsrp: String,                                   // Synchronization Signal RSRP (5G-specific)
    val ssRsrq: String,                                   // Synchronization Signal RSRQ (5G-specific)
    val ssSinr: String,                                   // Synchronization Signal SINR (5G-specific)
    val csiRsrp: String,                                  // CSI Reference Signal RSRP (5G-specific)
    val csiRsrq: String,                                  // CSI Reference Signal RSRQ (5G-specific)
    val csiSinr: String,                                  // CSI Signal-to-Interference-plus-Noise Ratio
)
