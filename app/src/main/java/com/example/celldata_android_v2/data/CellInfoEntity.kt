package com.example.celldata_android_v2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cell_info")
data class CellInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val net: String,
    val frequency: String,
    val mcc: String,
    val mnc: String,
    val iso: String,
    val eci: String,
    val eNb: String,
    val cid: String,
    val tac: String,
    val pci: String,
    val timestamp: String,
    val rssi: String,
    val rsrp: String,
    val rsrq: String,
    val snr: String,
    val signalStrength: String,

)
