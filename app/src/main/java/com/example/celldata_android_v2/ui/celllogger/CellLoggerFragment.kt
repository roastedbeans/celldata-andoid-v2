package com.example.celldata_android_v2.ui.celllogger

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.celldata_android_v2.data.DatabaseProvider
import com.example.celldata_android_v2.data.CellInfoEntity
import com.example.celldata_android_v2.databinding.FragmentCellLoggerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import android.util.Log

class CellLoggerFragment : Fragment() {

    private var _binding: FragmentCellLoggerBinding? = null
    private val binding get() = _binding!!

    private val displayedCellInfo = mutableListOf<CellInfoEntity>() // Cache for displayed rows

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCellLoggerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupDeleteButton()
        setupExportButton()

        // Periodically update data
        lifecycleScope.launch {
            while (true) {
                loadCellInfoData()
                delay(5000) // Update every 5 seconds (adjust as needed)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadCellInfoData() {
        lifecycleScope.launch {
            val cellInfoList = getCellInfoFromDatabase()

            if (cellInfoList != displayedCellInfo) {
                populateTable(cellInfoList)
                displayedCellInfo.clear()
                displayedCellInfo.addAll(cellInfoList) // Cache the current list
            }
        }
    }

    private suspend fun getCellInfoFromDatabase(): List<CellInfoEntity> {
        return withContext(Dispatchers.IO) {
            val database = DatabaseProvider.getDatabase(requireContext())
            database.cellInfoDao().getAllCellInfo()
        }
    }

    private fun populateTable(cellInfoList: List<CellInfoEntity>) {
        val table = binding.cellInfoTable

        // Clear existing rows (except the header row)
        table.removeViews(1, table.childCount - 1)

        // Add new rows
        for (cellInfo in cellInfoList) {
            val row = TableRow(requireContext())
            row.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            addTextViewToRow(row, cellInfo.net)
            addTextViewToRow(row, cellInfo.connectionStatus)
            addTextViewToRow(row, cellInfo.signalStrength)
            addTextViewToRow(row, cellInfo.timestamp)
            addTextViewToRow(row, cellInfo.frequency)
            addTextViewToRow(row, cellInfo.bandWidth)
            addTextViewToRow(row, cellInfo.mcc)
            addTextViewToRow(row, cellInfo.mnc)
            addTextViewToRow(row, cellInfo.iso)
            addTextViewToRow(row, cellInfo.eci)
            addTextViewToRow(row, cellInfo.eNb)
            addTextViewToRow(row, cellInfo.cid)
            addTextViewToRow(row, cellInfo.tac)
            addTextViewToRow(row, cellInfo.pci)
            addTextViewToRow(row, cellInfo.rssi)
            addTextViewToRow(row, cellInfo.rsrp)
            addTextViewToRow(row, cellInfo.rsrq)
            addTextViewToRow(row, cellInfo.sinr)
            addTextViewToRow(row, cellInfo.ssRsrp)
            addTextViewToRow(row, cellInfo.ssRsrq)
            addTextViewToRow(row, cellInfo.ssSinr)
            addTextViewToRow(row, cellInfo.csiRsrp)
            addTextViewToRow(row, cellInfo.csiRsrq)
            addTextViewToRow(row, cellInfo.csiSinr)

            table.addView(row)
        }
    }

    private fun addTextViewToRow(row: TableRow, text: String?) {
        val textView = TextView(requireContext()).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            setPadding(8, 8, 8, 8)
            this.text = text ?: "N/A"
        }
        row.addView(textView)
    }

    private fun setupDeleteButton() {
        val deleteButton: Button = binding.deleteDataButton
        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val database = DatabaseProvider.getDatabase(requireContext())
                    database.cellInfoDao().deleteAllCellInfo()
                }
                // Reload the table after deleting data
                loadCellInfoData()
            }
        }
    }

    private fun setupExportButton() {
        val exportButton: Button = binding.exportDataButton
        exportButton.setOnClickListener {
            lifecycleScope.launch {
                val context = requireContext()

                // Check and request permissions
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        requestStoragePermission()
                        return@launch
                    }
                } else if (!hasStoragePermission(context)) {
                    requestStoragePermission()
                    return@launch
                }

                withContext(Dispatchers.IO) {
                    try {
                        val database = DatabaseProvider.getDatabase(context)
                        val cellInfoList = database.cellInfoDao().getAllCellInfo()

                        // Log the number of rows
                        Log.d("ExportButton", "Exporting ${cellInfoList.size} rows of data")

                        if (cellInfoList.isEmpty()) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "No data to export.", Toast.LENGTH_LONG).show()
                            }
                            return@withContext
                        }

                        // Create CSV
                        val csvData = StringBuilder().apply {
                            append(
                                "Net,ConnectionStatus,SignalStrength,Timestamp,Frequency,BandWidth,MCC,MNC,ISO,ECI,eNb,CID,TAC,PCI,RSSI,RSRP,RSRQ,SINR,SS_RSRP,SS_RSRQ,SS_SINR,CSI_RSRP,CSI_RSRQ,CSI_SINR\n"
                            )
                            cellInfoList.forEach { cellInfo ->
                                append(
                                    "${cellInfo.net},${cellInfo.connectionStatus},${cellInfo.signalStrength},${cellInfo.timestamp}," +
                                            "${cellInfo.frequency},${cellInfo.bandWidth},${cellInfo.mcc},${cellInfo.mnc},${cellInfo.iso},${cellInfo.eci}," +
                                            "${cellInfo.eNb},${cellInfo.cid},${cellInfo.tac},${cellInfo.pci},${cellInfo.rssi},${cellInfo.rsrp},${cellInfo.rsrq}," +
                                            "${cellInfo.sinr},${cellInfo.ssRsrp},${cellInfo.ssRsrq},${cellInfo.ssSinr},${cellInfo.csiRsrp},${cellInfo.csiRsrq},${cellInfo.csiSinr}\n"
                                )
                            }
                        }

                        // Save file
                        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "cell_data_export.csv")
                        file.writeText(csvData.toString())

                        Log.d("ExportButton", "File saved to: ${file.absolutePath}")

                        // Notify user
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Data exported to: ${file.absolutePath}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("ExportButton", "Failed to export data", e)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Failed to export data: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }


    // Helper function to check storage permissions
    private fun hasStoragePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:" + requireContext().packageName)
                }
                startActivity(intent)
            } catch (e: Exception) {
                // Handle error, e.g., if Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION is not available
                Toast.makeText(requireContext(), "Unable to open permission settings.", Toast.LENGTH_SHORT).show()
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE_PERMISSION
            )
        }
    }


    // Define a constant for the storage permission request code
    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 1001
    }

}
