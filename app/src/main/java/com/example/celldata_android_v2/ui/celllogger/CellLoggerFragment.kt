package com.example.celldata_android_v2.ui.celllogger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.celldata_android_v2.data.DatabaseProvider
import com.example.celldata_android_v2.data.CellInfoEntity
import com.example.celldata_android_v2.databinding.FragmentCellLoggerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            addTextViewToRow(row, cellInfo.frequency)
            addTextViewToRow(row, cellInfo.mcc)
            addTextViewToRow(row, cellInfo.mnc)
            addTextViewToRow(row, cellInfo.iso)
            addTextViewToRow(row, cellInfo.eci)
            addTextViewToRow(row, cellInfo.eNb)
            addTextViewToRow(row, cellInfo.cid)
            addTextViewToRow(row, cellInfo.tac)
            addTextViewToRow(row, cellInfo.pci)
            addTextViewToRow(row, cellInfo.timestamp)
            addTextViewToRow(row, cellInfo.rssi)
            addTextViewToRow(row, cellInfo.rsrp)
            addTextViewToRow(row, cellInfo.rsrq)
            addTextViewToRow(row, cellInfo.snr)
            addTextViewToRow(row, cellInfo.signalStrength)

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
}
