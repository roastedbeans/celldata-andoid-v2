package com.example.celldata_android_v2.ui.celllogger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.celldata_android_v2.databinding.FragmentCellLoggerBinding

class CellLoggerFragment : Fragment() {

    private var _binding: FragmentCellLoggerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cellLoggerViewModel =
            ViewModelProvider(this)[CellLoggerViewModel::class.java]

        _binding = FragmentCellLoggerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCellLogger
        cellLoggerViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}