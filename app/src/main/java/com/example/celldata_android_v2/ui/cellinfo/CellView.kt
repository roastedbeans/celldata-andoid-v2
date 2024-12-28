package com.example.celldata_android_v2.ui.cellinfo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import cz.mroczis.netmonster.core.model.cell.*

class CellView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    private val transformer = object : ICellProcessor<Unit> {

        override fun processLte(cell: CellLte) {
            cell.network?.let { addView("NET", "LTE ${cell.connectionStatus.javaClass.simpleName}") }
            cell.band?.let { band ->
                band.channelNumber.let { addView("FREQ", "$it (#${band.number}, ${band.name})") }
            }

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

            cell.timestamp?.let { addView("Timestamp", it) }
            cell.signal.let { signal ->
                signal.rssi?.let { addView("RSSI", it) }
                signal.rsrp?.let { addView("RSRP", it) }
                signal.rsrq?.let { addView("RSRQ", it) }
                signal.cqi?.let { addView("CQI", it) }
                signal.timingAdvance?.let { addView("TA", it) }
                signal.snr?.let { addView("SNR", it) }

                val signalStrength = signal.rsrp?.let { rsrp ->
                    when {
                        rsrp >= -85 -> 4  // Excellent
                        rsrp >= -95 -> 3  // Good
                        rsrp >= -105 -> 2 // Fair
                        rsrp >= -115 -> 1 // Poor
                        else -> 0         // No signal
                    }
                } ?: 0

                addView("Signal Strength", signalStrength)
            }
        }

        override fun processCdma(cell: CellCdma) {
            addView("SID", cell.sid)
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

                val signalStrength = signal.cdmaRssi?.let { rssi ->
                    when {
                        rssi >= -75 -> 4  // Excellent
                        rssi >= -85 -> 3  // Good
                        rssi >= -95 -> 2  // Fair
                        rssi >= -100 -> 1 // Poor
                        else -> 0         // No signal
                    }
                } ?: 0

                addView("Signal Strength", signalStrength)
            }
        }

        override fun processGsm(cell: CellGsm) {
            cell.network?.let { addView("NET", "GSM ${cell.connectionStatus.javaClass.simpleName}") }
            cell.band?.let { band ->
                band.channelNumber.let { addView("FREQ", "$it (#${band.number}, ${band.name})") }
            }

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

                val signalStrength = signal.rssi?.let { rssi ->
                    when {
                        rssi >= -70 -> 4  // Excellent
                        rssi >= -85 -> 3  // Good
                        rssi >= -100 -> 2 // Fair
                        rssi >= -110 -> 1 // Poor
                        else -> 0         // No signal
                    }
                } ?: 0

                addView("Signal Strength", signalStrength)
            }
        }

        override fun processNr(cell: CellNr) {
            cell.network?.let { addView("NET", "NR ${cell.connectionStatus.javaClass.simpleName}") }
            cell.band?.let { band ->
                band.channelNumber.let { addView("FREQ", "$it (#${band.number}, ${band.name})") }
            }
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
                val signalStrength = signal.ssRsrp?.let { ssRsrp ->
                    when {
                        ssRsrp >= -80 -> 4  // Excellent
                        ssRsrp >= -90 -> 3  // Good
                        ssRsrp >= -100 -> 2 // Fair
                        ssRsrp >= -110 -> 1 // Poor
                        else -> 0           // No signal
                    }
                } ?: 0

                addView("Signal Strength", signalStrength)
            }
        }

        override fun processTdscdma(cell: CellTdscdma) {
            cell.network?.let { addView("NET", "TDS-CDMA ${cell.connectionStatus.javaClass.simpleName}") }
            cell.band?.let { band ->
                band.channelNumber.let { addView("FREQ", "$it (#${band.number}, ${band.name})") }
            }

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

                val signalStrength = signal.rssi?.let { rssi ->
                    when {
                        rssi >= -70 -> 4  // Excellent
                        rssi >= -85 -> 3  // Good
                        rssi >= -100 -> 2 // Fair
                        rssi >= -110 -> 1 // Poor
                        else -> 0         // No signal
                    }
                } ?: 0

                addView("Signal Strength", signalStrength)
            }
        }

        override fun processWcdma(cell: CellWcdma) {
            cell.network?.let { addView("NET", "WCDMA ${cell.connectionStatus.javaClass.simpleName}") }
            cell.band?.let { band ->
                band.channelNumber.let { addView("FREQ", "$it (#${band.number}, ${band.name})") }
            }

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

                val signalStrength = signal.rscp?.let { rscp ->
                    when {
                        rscp >= -75 -> 4  // Excellent
                        rscp >= -85 -> 3  // Good
                        rscp >= -95 -> 2  // Fair
                        rscp >= -105 -> 1 // Poor
                        else -> 0         // No signal
                    }
                } ?: 0

                addView("Signal Strength", signalStrength)
            }
        }

    }


    fun bind (cell: ICell) {
        removeAllViews()
        cell.let(transformer)
    }

    private fun addView(title: String, message: Any) {
        val view = CellItemSimple(context).apply {
            bind(title, message.toString())
        }

        Log.d("view","$view");
        addView(view)
    }


}