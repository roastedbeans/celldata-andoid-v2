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

/**
 * CellInfoFragment
 *
 * UI component responsible for displaying real-time cellular network data.
 * This fragment manages the visual representation of collected network information
 * and handles lifecycle-aware data updates.
 *
 * Key Components:
 * - RecyclerView for efficient data display
 * - ViewModel integration for data management
 * - Permission handling
 * - Lifecycle management
 *
 * Display Features:
 * - Real-time updates of network parameters
 * - Scrollable list of cell information
 * - Optimized performance for continuous updates
 */
class CellInfoFragment : Fragment() {
    /**
     * ViewModel instance for cellular data management.
     * Handles data collection and updates.
     */
    private lateinit var cellInfoViewModel: CellInfoViewModel

    /**
     * ViewBinding instance for type-safe view access.
     * Null when fragment view is destroyed.
     */
    private var _binding: FragmentCellInfoBinding? = null
    private val binding get() = _binding!!

    /**
     * Adapter instance for RecyclerView.
     * Manages the display of cellular network data items.
     */
    private val adapter = MainAdapter()

    /**
     * Creates and initializes the fragment's UI.
     * Sets up the RecyclerView and data observation.
     *
     * Implementation:
     * 1. Initializes ViewModel
     * 2. Sets up view binding
     * 3. Configures RecyclerView
     * 4. Establishes data observation
     */
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

    /**
     * Configures RecyclerView for optimal performance.
     *
     * Configuration:
     * - Linear layout for vertical scrolling
     * - Fixed size optimization
     * - Disabled item animations for better performance
     * - Custom adapter for cell data display
     */
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CellInfoFragment.adapter
            // Add these lines for better scrolling performance
            setHasFixedSize(true)
            itemAnimator = null
        }
    }

    /**
     * Establishes observation of cellular data updates.
     * Updates the RecyclerView adapter when new data arrives.
     */
    fun observeCellData() {
        cellInfoViewModel.cellData.observe(viewLifecycleOwner) { cells ->
            adapter.updateData(cells)
        }
    }

    /**
     * Lifecycle method: Fragment becomes visible.
     * Starts data updates if permissions are granted.
     */
    override fun onResume() {
        super.onResume()
        cellInfoViewModel.startUpdates()
    }

    /**
     * Lifecycle method: Fragment is being paused.
     * Stops data updates to conserve resources.
     */
    override fun onPause() {
        super.onPause()
        cellInfoViewModel.startUpdates()
    }

    /**
     * Callback for permission grant events.
     * Initiates data updates when permissions are granted.
     *
     * Usage:
     * Called from activity when permissions are granted by user
     */
    fun onPermissionsGranted() {
        cellInfoViewModel.startUpdates()
    }

    /**
     * Lifecycle method: Fragment view is being destroyed.
     * Cleans up ViewBinding to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}