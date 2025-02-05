package com.example.celldata_android_v2.ui.cellinfo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import com.example.celldata_android_v2.databinding.ViewCellItemSimpleBinding

/**
 * CellItemSimple
 * A custom view component for displaying individual cellular network parameters.
 * Used within CellView to create a consistent layout for displaying various network metrics
 * and information collected during network analysis.
 *
 * Layout Structure:
 * - Linear layout containing two text views:
 *   1. titleView: Parameter name/identifier
 *   2. messageView: Parameter value/data
 *
 * Features:
 * - Standardized display format for network parameters
 * - Consistent styling across all cellular data points
 * - Reusable component for different network technologies
 *
 * Usage Example:
 * val cellItem = CellItemSimple(context)
 * cellItem.bind("RSRP", "-85 dBm")
 */
class CellItemSimple @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * ViewBinding instance for accessing layout elements.
     * Uses view_cell_item_simple.xml layout resource.
     */
    private val binding: ViewCellItemSimpleBinding

    /**
     * Initializes the view by inflating the layout and setting up initial configurations.
     * The layout is inflated using ViewBinding for type-safe view access.
     */
    init {
        binding = ViewCellItemSimpleBinding.inflate(LayoutInflater.from(context), this, true)
    }

    /**
     * Binds data to the view, updating both the title and message text fields.
     *
     * @param title The parameter name or identifier to be displayed (e.g., "RSRP", "PCI", "TAC")
     * @param message The corresponding value or data to be displayed
     *
     * Usage:
     * cellItem.bind("Signal Strength", "Excellent (-75 dBm)")
     */
    fun bind(title: String, message: String) {
        binding.titleView.text = title
        binding.messageView.text = message
    }
}