package com.example.celldata_android_v2.ui.cellinfo

import com.example.celldata_android_v2.data.DatabaseProvider
import com.example.celldata_android_v2.data.CellInfoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import cz.mroczis.netmonster.core.model.cell.*
import cz.mroczis.netmonster.core.model.signal.SignalCdma
import cz.mroczis.netmonster.core.model.signal.SignalGsm
import cz.mroczis.netmonster.core.model.signal.SignalLte
import cz.mroczis.netmonster.core.model.signal.SignalNr
import cz.mroczis.netmonster.core.model.signal.SignalTdscdma
import cz.mroczis.netmonster.core.model.signal.SignalWcdma

class CellView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val dao = DatabaseProvider.getDatabase(context).cellInfoDao()

    init {
        orientation = VERTICAL
    }

    private val transformer = object : ICellProcessor<Unit> {

        override fun processLte(cell: CellLte) {
            cell.network?.let { addView("NET", "LTE") }
            addView("Connection Status", cell.connectionStatus.javaClass.simpleName)
            addView("BandWidth", cell.bandwidth.toString())
            cell.band?.let { band ->
                addView("FREQUENCY", "${band.channelNumber} (#${band.number}, ${band.name})")
            }
            cell.timestamp?.let { addView("Timestamp", it) }
            cell.network?.mcc?.let {addView("MCC", it)}
            cell.network?.mnc?.let {addView("MNC", it)}
            cell.network?.iso?.let {addView("ISO", it)}
            cell.eci?.let { addView("ECI", it) }
            cell.enb?.let { addView("eNb", it) }
            cell.cid?.let { addView("CID", it) }
            cell.tac?.let { addView("TAC", it) }
            cell.pci?.let { addView("PCI", it) }
            cell.bandwidth?.let { addView("BW", it) }
            cell.aggregatedBands.joinToString { "${it.name} (#${it.number})" }.takeIf { it.isNotEmpty() }?.let {
                addView("Agg. Bands", it)
            }
            cell.signal.let { signal ->
                signal.rssi?.let { addView("RSSI", it) }
                signal.rsrp?.let { addView("RSRP", it) }
                signal.rsrq?.let { addView("RSRQ", it) }
                signal.cqi?.let { addView("CQI", it) }
                signal.timingAdvance?.let { addView("TA", it) }
                signal.snr?.let { addView("SNR", it) }
                val signalStrength = calculateLteSignalStrength(signal)
                addView("Signal Strength", signalStrength)
            }
        }

        override fun processCdma(cell: CellCdma) {
            cell.network?.let { addView("NET", "CDMA") }
            addView("Connection Status", cell.connectionStatus.javaClass.simpleName)
            cell.band?.let { band ->
                addView("FREQUENCY", "${band.channelNumber} (#${band.number}, ${band.name})")
            }
            cell.timestamp?.let { addView("Timestamp", it) }
            cell.network?.mcc?.let {addView("MCC", it)}
            cell.network?.mnc?.let {addView("MNC", it)}
            cell.network?.iso?.let {addView("ISO", it)}
            cell.nid?.let { addView("NID", it) }
            cell.bid?.let { addView("BID", it) }
            cell.lat?.let { addView("LAT", it) }
            cell.lon?.let { addView("LON", it) }
            cell.signal.let { signal ->
                signal.cdmaEcio?.let { addView("CD EC/IO", it) }
                signal.cdmaRssi?.let { addView("CD RSSI", it) }
                signal.evdoEcio?.let { addView("EV EC/IO", it) }
                signal.evdoRssi?.let { addView("EV RSSI", it) }
                signal.evdoSnr?.let { addView("EV SNR", it) }
                val signalStrength = calculateCdmaSignalStrength(signal)
                addView("Signal Strength", signalStrength)
            }
        }

        override fun processGsm(cell: CellGsm) {
            cell.network?.let { addView("NET", "GSM") }
            addView("Connection Status", cell.connectionStatus.javaClass.simpleName)
            cell.band?.let { band ->
                addView("FREQUENCY", "${band.channelNumber} (#${band.number}, ${band.name})")
            }
            cell.timestamp?.let { addView("Timestamp", it) }
            cell.network?.mcc?.let {addView("MCC", it)}
            cell.network?.mnc?.let {addView("MNC", it)}
            cell.network?.iso?.let {addView("ISO", it)}
            cell.cid?.let { addView("CID", it) }
            cell.lac?.let { addView("LAC", it) }
            cell.bsic?.let { addView("BSIC", it) }
            cell.signal.let { signal ->
                signal.rssi?.let { addView("RSSI", it) }
                signal.bitErrorRate?.let { addView("BER", it) }
                signal.timingAdvance?.let { addView("TA", it) }
                val signalStrength = calculateGsmSignalStrength(signal)
                addView("Signal Strength", signalStrength)
            }
        }

        override fun processNr(cell: CellNr) {
            cell.network?.let { addView("NET", "5G NR") }
            addView("Connection Status", cell.connectionStatus.javaClass.simpleName)
            cell.band?.let { band ->
                addView("FREQUENCY", "${band.channelNumber} (#${band.number}, ${band.name})")
            }
            cell.timestamp?.let { addView("Timestamp", it) }
            cell.network?.mcc?.let {addView("MCC", it)}
            cell.network?.mnc?.let {addView("MNC", it)}
            cell.network?.iso?.let {addView("ISO", it)}
            cell.nci?.let { addView("NCI", it) }
            cell.tac?.let { addView("TAC", it) }
            cell.pci?.let { addView("PCI", it) }
            cell.signal.let { signal ->
                signal.csiRsrp?.let { addView("CSI RSRP", it) }
                signal.csiRsrq?.let { addView("CSI RSRQ", it) }
                signal.csiSinr?.let { addView("CSI SINR", it) }
                signal.ssRsrp?.let { addView("SS RSRP", it) }
                signal.ssRsrq?.let { addView("SS RSRQ", it) }
                signal.ssSinr?.let { addView("SS SINR", it) }
                val signalStrength = calculateNrSignalStrength(signal)
                addView("Signal Strength", signalStrength)
            }
        }

        override fun processTdscdma(cell: CellTdscdma) {
            cell.network?.let { addView("NET", "TDS-CDMA") }
            addView("Connection Status", cell.connectionStatus.javaClass.simpleName)
            cell.band?.let { band ->
                addView("FREQUENCY", "${band.channelNumber} (#${band.number}, ${band.name})")
            }
            cell.timestamp?.let { addView("Timestamp", it) }
            cell.network?.mcc?.let {addView("MCC", it)}
            cell.network?.mnc?.let {addView("MNC", it)}
            cell.network?.iso?.let {addView("ISO", it)}
            cell.ci?.let { addView("CI", it) }
            cell.rnc?.let { addView("RNC", it) }
            cell.cid?.let { addView("CID", it) }
            cell.lac?.let { addView("LAC", it) }
            cell.cpid?.let { addView("CPID", it) }
            cell.signal.let { signal ->
                signal.rssi?.let { addView("RSSI", it) }
                signal.bitErrorRate?.let { addView("BER", it) }
                signal.rscp?.let { addView("RSCP", it) }
                val signalStrength = calculateTdscdmaSignalStrength(signal)
                addView("Signal Strength", signalStrength)
            }
        }

        override fun processWcdma(cell: CellWcdma) {
            cell.network?.let { addView("NET", "WCDMA") }
            addView("Connection Status", cell.connectionStatus.javaClass.simpleName)
            cell.band?.let { band ->
                addView("FREQUENCY", "${band.channelNumber} (#${band.number}, ${band.name})")
            }
            cell.timestamp?.let { addView("Timestamp", it) }
            cell.network?.mcc?.let {addView("MCC", it)}
            cell.network?.mnc?.let {addView("MNC", it)}
            cell.network?.iso?.let {addView("ISO", it)}
            cell.ci?.let { addView("CI", it) }
            cell.rnc?.let { addView("RNC", it) }
            cell.cid?.let { addView("CID", it) }
            cell.lac?.let { addView("LAC", it) }
            cell.psc?.let { addView("PSC", it) }
            cell.signal.let { signal ->
                signal.rssi?.let { addView("RSSI", it) }
                signal.bitErrorRate?.let { addView("BER", it) }
                signal.rscp?.let { addView("RSCP", it) }
                signal.ecio?.let { addView("ECIO", it) }
                signal.ecno?.let { addView("ECNO", it) }
                val signalStrength = calculateWcdmaSignalStrength(signal)
                addView("Signal Strength", signalStrength)
            }
        }
    }

    fun bind (cell: ICell) {
        removeAllViews()
        cell.let(transformer)

        val cellInfoEntity = mapToCellInfoEntity(cell)
        saveToDatabase(cellInfoEntity)
    }

    private fun saveToDatabase(cellInfoEntity: CellInfoEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertCellInfo(cellInfoEntity)
        }
    }

    private fun mapToCellInfoEntity(cell: ICell): CellInfoEntity {
        Log.d("Cell", "$cell")
        return when (cell) {
            is CellLte -> {
                val signal = cell.signal
                val network = cell.network
                val band = cell.band

                CellInfoEntity(
                    net = "LTE",
                    connectionStatus =  cell.connectionStatus.javaClass.simpleName.orEmpty(),
                    signalStrength = calculateLteSignalStrength(signal),
                    timestamp = cell.timestamp.toString(),
                    frequency = band?.downlinkEarfcn?.toString().orEmpty(),
                    bandWidth = cell.bandwidth.toString(),
                    mcc = network?.mcc.orEmpty(),
                    mnc = network?.mnc.orEmpty(),
                    iso = network?.iso.orEmpty(),
                    eci = cell.eci.toString(),
                    eNb = cell.enb.toString(),
                    cid = cell.cid.toString(),
                    tac = cell.tac.toString(),
                    pci = cell.pci.toString(),
                    rssi = signal.rssi?.toString().orEmpty(),
                    rsrp = signal.rsrp?.toString().orEmpty(),
                    sinr = signal.snr?.toString().orEmpty(),
                    rsrq = signal.rsrq?.toString().orEmpty(),
                    ssRsrp = "N/A",
                    ssRsrq = "N/A",
                    ssSinr = "N/A",
                    csiRsrp = "N/A",
                    csiRsrq = "N/A",
                    csiSinr = "N/A",
                )
            }

            is CellNr -> {
                val signal = cell.signal
                val network = cell.network
                val band = cell.band

                CellInfoEntity(
                    net = "5G NR",
                    connectionStatus = cell.connectionStatus.javaClass.simpleName.orEmpty(),
                    signalStrength = calculateNrSignalStrength(signal),
                    timestamp = cell.timestamp.toString(),
                    frequency = band?.downlinkFrequency?.toString().orEmpty(),
                    bandWidth = estimateBandwidthFromArfcn(band?.downlinkArfcn),
                    mcc = network?.mcc.orEmpty(),
                    mnc = network?.mnc.orEmpty(),
                    iso = network?.iso.orEmpty(),
                    eci = "N/A",
                    eNb = "N/A",
                    cid = "N/A",
                    tac = cell.tac.toString(),
                    pci = cell.pci.toString(),
                    rsrp = "N/A",
                    rsrq = "N/A",
                    sinr = "N/A",
                    rssi = "N/A",
                    ssRsrp = cell.signal.ssRsrp.toString(),
                    ssRsrq = cell.signal.ssRsrq.toString(),
                    ssSinr = cell.signal.ssSinr.toString(),
                    csiRsrp = cell.signal.csiRsrp.toString(),
                    csiRsrq = cell.signal.csiRsrq.toString(),
                    csiSinr = cell.signal.csiSinr.toString(),
                )
            }

            is CellCdma -> {
                val signal = cell.signal
                val network = cell.network
                val band = cell.band

                CellInfoEntity(
                    net = "CDMA",
                    connectionStatus = cell.connectionStatus.javaClass.simpleName.orEmpty(),
                    frequency = "N/A",
                    bandWidth = "N/A",
                    mcc = network?.mcc.orEmpty(),
                    mnc = network?.mnc.orEmpty(),
                    iso = network?.iso.orEmpty(),
                    eci = "N/A",
                    eNb = "N/A",
                    cid = "N/A",
                    tac = "N/A",
                    pci = "N/A",
                    timestamp = cell.timestamp.toString(),
                    signalStrength = calculateCdmaSignalStrength(signal),
                    rssi = signal.cdmaRssi?.toString().orEmpty(),
                    rsrp = "N/A",
                    rsrq = "N/A",
                    sinr = signal.evdoSnr?.toString().orEmpty(),
                    ssRsrp = "N/A",
                    ssRsrq = "N/A",
                    ssSinr = "N/A",
                    csiRsrp = "N/A",
                    csiRsrq = "N/A",
                    csiSinr = "N/A",
                )
            }

            is CellWcdma -> {
                val signal = cell.signal
                val network = cell.network
                val band = cell.band

                CellInfoEntity(
                    net = "WCDMA",
                    connectionStatus = cell.connectionStatus.javaClass.simpleName.orEmpty(),
                    frequency = "N/A",
                    bandWidth = "N/A",
                    mcc = network?.mcc.orEmpty(),
                    mnc = network?.mnc.orEmpty(),
                    iso = network?.iso.orEmpty(),
                    eci = "N/A",
                    eNb = "N/A",
                    cid = "N/A",
                    tac = "N/A",
                    pci = "N/A",
                    timestamp = cell.timestamp.toString(),
                    signalStrength = calculateWcdmaSignalStrength(signal),
                    rssi = signal.rssi?.toString().orEmpty(),
                    rsrp = "N/A",
                    rsrq = "N/A",
                    sinr = "N/A",
                    ssRsrp = "N/A",
                    ssRsrq = "N/A",
                    ssSinr = "N/A",
                    csiRsrp = "N/A",
                    csiRsrq = "N/A",
                    csiSinr = "N/A",
                )
            }

            is CellTdscdma -> {
                val signal = cell.signal
                val network = cell.network
                val band = cell.band

                CellInfoEntity(
                    net = "TDS-CDMA",
                    connectionStatus =  cell.connectionStatus.javaClass.simpleName.orEmpty(),
                    frequency = "N/A",
                    bandWidth = "N/A",
                    mcc = network?.mcc.orEmpty(),
                    mnc = network?.mnc.orEmpty(),
                    iso = network?.iso.orEmpty(),
                    eci = "N/A",
                    eNb = "N/A",
                    cid = "N/A",
                    tac = "N/A",
                    pci = "N/A",
                    timestamp = cell.timestamp.toString(),
                    signalStrength = calculateTdscdmaSignalStrength(signal),
                    rssi = signal.rssi?.toString().orEmpty(),
                    rsrp = "N/A",
                    rsrq = "N/A",
                    sinr = "N/A",
                    ssRsrp = "N/A",
                    ssRsrq = "N/A",
                    ssSinr = "N/A",
                    csiRsrp = "N/A",
                    csiRsrq = "N/A",
                    csiSinr = "N/A",
                )
            }

            is CellGsm -> {
                val signal = cell.signal
                val network = cell.network
                val band = cell.band

                CellInfoEntity(
                    net = "GSM",
                    connectionStatus =  cell.connectionStatus.javaClass.simpleName.orEmpty(),
                    frequency = "N/A",
                    bandWidth = "N/A",
                    mcc = network?.mcc.orEmpty(),
                    mnc = network?.mnc.orEmpty(),
                    iso = network?.iso.orEmpty(),
                    eci = "N/A",
                    eNb = "N/A",
                    cid = "N/A",
                    tac = "N/A",
                    pci = "N/A",
                    timestamp = cell.timestamp.toString(),
                    signalStrength = calculateGsmSignalStrength(signal),
                    rssi = signal.rssi?.toString().orEmpty(),
                    rsrp = "N/A",
                    rsrq = "N/A",
                    sinr = "N/A",
                    ssRsrp = "N/A",
                    ssRsrq = "N/A",
                    ssSinr = "N/A",
                    csiRsrp = "N/A",
                    csiRsrq = "N/A",
                    csiSinr = "N/A",
                )
            }

            else -> CellInfoEntity(
                net = "Unknown",
                connectionStatus = "",
                signalStrength = "",
                timestamp = "",
                frequency = "",
                bandWidth = "",
                mcc = "",
                mnc = "",
                iso = "",
                eci = "",
                eNb = "",
                cid = "",
                tac = "",
                pci = "",
                rssi = "",
                rsrp = "",
                rsrq = "",
                sinr = "",
                ssRsrp = "",
                ssRsrq = "",
                ssSinr = "",
                csiRsrp = "",
                csiRsrq = "",
                csiSinr = ""
            )
        }
    }

    private fun estimateBandwidthFromArfcn(downlinkArfcn: Int?): String {
        return if (downlinkArfcn == null) {
            "N/A"
        } else {
            when (downlinkArfcn) {
                in 151600..160600 -> "100 MHz (n78, 3.5 GHz)"
                in 173800..178800 -> "50 MHz (n79, 4.8 GHz)"
                in 422000..434000 -> "200 MHz (n260, 39 GHz)"
                else -> "Unknown Bandwidth"
            }
        }
    }

    private fun calculateLteSignalStrength(signal: SignalLte?): String {
        return (signal?.rsrp?.let { rsrp ->
            when {
                rsrp >= -80 -> 4  // Excellent
                rsrp >= -90 -> 3  // Good
                rsrp >= -100 -> 2 // Fair
                rsrp >= -110 -> 1 // Poor
                else -> 0           // No signal
            }
        } ?: 0).toString()
    }

    private fun calculateNrSignalStrength(signal: SignalNr?): String {
        return (signal?.ssRsrp?.let { ssRsrp ->
            when {
                ssRsrp >= -80 -> 4  // Excellent
                ssRsrp >= -90 -> 3  // Good
                ssRsrp >= -100 -> 2 // Fair
                ssRsrp >= -110 -> 1 // Poor
                else -> 0           // No signal
            }
        } ?: 0).toString()
    }

    private fun calculateCdmaSignalStrength(signal: SignalCdma?): String {
        return (signal?.cdmaRssi?.let { rssi ->
            when {
                rssi >= -75 -> 4
                rssi >= -85 -> 3
                rssi >= -95 -> 2
                rssi >= -100 -> 1
                else -> 0
            }
        } ?: 0).toString()
    }

    private fun calculateWcdmaSignalStrength(signal: SignalWcdma?): String {
        return (signal?.rssi?.let { rssi ->
            when {
                rssi >= -75 -> 4
                rssi >= -85 -> 3
                rssi >= -95 -> 2
                rssi >= -100 -> 1
                else -> 0
            }
        } ?: 0).toString()
    }

    private fun calculateTdscdmaSignalStrength(signal: SignalTdscdma?): String {
        return (signal?.rssi?.let { rssi ->
            when {
                rssi >= -75 -> 4
                rssi >= -85 -> 3
                rssi >= -95 -> 2
                rssi >= -100 -> 1
                else -> 0
            }
        } ?: 0).toString()
    }

    private fun calculateGsmSignalStrength(signal: SignalGsm?): String {
        return (signal?.rssi?.let { rssi ->
            when {
                rssi >= -75 -> 4
                rssi >= -85 -> 3
                rssi >= -95 -> 2
                rssi >= -100 -> 1
                else -> 0
            }
        } ?: 0).toString()
    }

    private fun addView(title: String, message: Any) {
        val view = CellItemSimple(context).apply {
            bind(title, message.toString())
        }
        addView(view)
    }
}