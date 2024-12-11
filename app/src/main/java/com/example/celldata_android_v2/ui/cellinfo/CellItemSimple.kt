package com.example.celldata_android_v2.ui.cellinfo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import com.example.celldata_android_v2.databinding.ViewCellItemSimpleBinding

class CellItemSimple @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewCellItemSimpleBinding

    init {
        binding = ViewCellItemSimpleBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(title: String, message: String) {
        binding.titleView.text = title
        binding.messageView.text = message
    }
}