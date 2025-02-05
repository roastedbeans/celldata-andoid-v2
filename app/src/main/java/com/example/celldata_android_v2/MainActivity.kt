package com.example.celldata_android_v2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.celldata_android_v2.databinding.ActivityMainBinding
import com.example.celldata_android_v2.ui.cellinfo.CellInfoFragment
import com.example.celldata_android_v2.ui.celllogger.CellLoggerFragment

/**
 * MainActivity
 *
 * Primary activity for the AirGap research project's Android implementation.
 * Orchestrates the application's main functionality for cellular network data collection
 * and analysis, with a focus on 5G network vulnerability research.
 *
 * Key Components:
 * 1. Navigation Management
 *    - Bottom navigation for switching between data collection modes
 *    - Fragment container for different visualization screens
 *
 * 2. Permission Handling
 *    - Runtime permission management for sensitive data access
 *    - Coordination with fragments for permission states
 *
 * 3. Window Management
 *    - System insets handling for proper UI display
 *    - Layout optimization for different screen sizes
 */
class MainActivity : AppCompatActivity() {

    /**
     * ViewBinding instance for type-safe view access.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Initializes the activity and sets up core components.
     *
     * Setup Process:
     * 1. Inflates layout using ViewBinding
     * 2. Configures bottom navigation
     * 3. Sets up window insets
     * 4. Loads default fragment
     * 5. Initiates permission checks
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        setupWindowInsets()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(CellInfoFragment())
        }

        checkAndRequestPermissions()
    }

    /**
     * Configures bottom navigation bar functionality.
     * Manages transitions between different data collection modes:
     * - Cell Information: Real-time network data display
     * - Cell Logger: Data logging and analysis tools
     */
    private fun setupBottomNavigation() {
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_cell_info -> {
                    loadFragment(CellInfoFragment())
                    true
                }
                R.id.navigation_cell_logger -> {
                    loadFragment(CellLoggerFragment())
                    true
                }
                else -> false
            }
        }
    }
    /**
     * Configures window insets for proper UI display.
     * Handles system bars and ensures correct layout padding.
     *
     * Implementation:
     * - Applies system bar insets
     * - Updates layout margins
     * - Adjusts fragment container padding
     */

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
            }

            binding.fragmentContainer.updatePadding(
                top = insets.top,
                bottom = insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * Loads specified fragment into the container.
     * Manages fragment transitions for different data collection modes.
     *
     * @param fragment The fragment to be loaded
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)  // Fixed: Use fragment_container instead of navigation_cell_info
            .commit()
    }

    /**
     * Manages permission checks and requests for cellular data access.
     *
     * Required Permissions:
     * - ACCESS_COARSE_LOCATION: For cell location data
     * - ACCESS_FINE_LOCATION: For precise location
     * - READ_PHONE_STATE: For detailed cell information
     */
    private fun checkAndRequestPermissions() {
        if (!hasRequiredPermissions() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
            ), PERMISSION_REQUEST_CODE)
        } else if (hasRequiredPermissions()) {
            // If permissions are already granted, notify the current fragment
            getCurrentCellInfoFragment()?.onPermissionsGranted()
        }
    }

    /**
     * Verifies if all required permissions are granted.
     *
     * Checks:
     * - Location permissions
     * - Phone state access
     *
     * @return Boolean indicating if all permissions are granted
     */
    private fun hasRequiredPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Retrieves the current CellInfoFragment if active.
     * Used for permission-related callbacks.
     *
     * @return Current CellInfoFragment instance or null
     */
    private fun getCurrentCellInfoFragment(): CellInfoFragment? {
        return supportFragmentManager.findFragmentById(R.id.fragment_container) as? CellInfoFragment
    }

    /**
     * Handles permission request results.
     * Notifies relevant components about permission changes.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, notify current fragment
                getCurrentCellInfoFragment()?.onPermissionsGranted()
            }
        }
    }

    /**
     * Companion object containing configuration constants.
     */
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}