package com.example.celldata_android_v2

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import cz.mroczis.netmonster.core.factory.NetMonsterFactory
import com.example.celldata_android_v2.databinding.ActivityMainBinding
import com.example.celldata_android_v2.ui.cellinfo.CellInfoFragment
import com.example.celldata_android_v2.ui.celllogger.CellLoggerFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REFRESH_RATIO = 5_000L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val adapter = MainAdapter()

    private lateinit var binding: ActivityMainBinding

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
    }

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

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                leftMargin = insets.left
                rightMargin = insets.right
            }

            binding.fragmentContainer.updatePadding(bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private fun checkAndRequestPermissions() {
        if (hasRequiredPermissions()) {
            loop()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            ), 0)
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
    }

    private fun loop() {
        updateData()
        handler.postDelayed(REFRESH_RATIO) { loop() }
    }

    @SuppressLint("MissingPermission")
    private fun updateData() {
        NetMonsterFactory.get(this).apply {
            val merged = getCells()
            adapter.data = merged
            Log.d("NTM-RES", " \n${merged.joinToString(separator = "\n")}")
        }
    }
}