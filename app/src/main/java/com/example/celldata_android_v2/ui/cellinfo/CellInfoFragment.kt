package com.example.celldata_android_v2.ui.cellinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.mroczis.netmonster.core.model.cell.ICell
import com.example.celldata_android_v2.databinding.FragmentCellInfoBinding

class CellInfoFragment : Fragment() {

    private lateinit var cellInfoViewModel: CellInfoViewModel
    private var _binding: FragmentCellInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cellInfoViewModel =
            ViewModelProvider(this)[CellInfoViewModel::class.java]

        _binding = FragmentCellInfoBinding.inflate(inflater, container, false)

        val cellView: CellView = binding.textCellInfo

        // Observe cell data from ViewModel and bind to CellView
        cellInfoViewModel.cellData.observe(viewLifecycleOwner) { cell ->
            if (cell != null) {
                cellView.bind(cell)
            }
        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}