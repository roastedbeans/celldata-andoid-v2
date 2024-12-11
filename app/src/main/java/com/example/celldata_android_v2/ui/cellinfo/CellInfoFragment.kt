package com.example.celldata_android_v2.ui.cellinfo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.celldata_android_v2.MainAdapter
import com.example.celldata_android_v2.databinding.FragmentCellInfoBinding

class CellInfoFragment : Fragment() {
    private lateinit var cellInfoViewModel: CellInfoViewModel
    private var _binding: FragmentCellInfoBinding? = null
    private val binding get() = _binding!!
    private val adapter = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cellInfoViewModel = ViewModelProvider(this)[CellInfoViewModel::class.java]
        _binding = FragmentCellInfoBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeCellData()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CellInfoFragment.adapter
            // Add these lines for better scrolling performance
            setHasFixedSize(true)
            itemAnimator = null
        }
    }

    private fun observeCellData() {
        cellInfoViewModel.cellData.observe(viewLifecycleOwner) { cells ->
            adapter.updateData(cells)
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        if (hasRequiredPermissions()) {
            cellInfoViewModel.startUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        cellInfoViewModel.stopUpdates()
    }

    fun onPermissionsGranted() {
        cellInfoViewModel.startUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}