// MainVH.kt
package com.example.celldata_android_v2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.mroczis.netmonster.core.model.cell.ICell
import com.example.celldata_android_v2.databinding.ViewCellBinding
import com.example.celldata_android_v2.ui.cellinfo.CellView

class MainVH(
    private val binding: ViewCellBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): MainVH {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewCellBinding.inflate(inflater, parent, false)
            return MainVH(binding)
        }
    }

    fun bind(cell: ICell) {
        binding.cellView.bind(cell)
    }
}