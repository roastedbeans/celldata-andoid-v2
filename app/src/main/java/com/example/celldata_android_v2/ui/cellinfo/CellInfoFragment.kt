package com.example.celldata_android_v2.ui.cellinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.celldata_android_v2.databinding.FragmentCellInfoBinding

class CellInfoFragment : Fragment() {

    private var _binding: FragmentCellInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cellInfoViewModel =
            ViewModelProvider(this)[CellInfoViewModel::class.java]

        _binding = FragmentCellInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCellInfo
        cellInfoViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}