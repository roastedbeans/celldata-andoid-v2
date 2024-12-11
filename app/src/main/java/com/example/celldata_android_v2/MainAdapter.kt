package com.example.celldata_android_v2

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.mroczis.netmonster.core.model.cell.ICell

class MainAdapter : RecyclerView.Adapter<MainVH>() {

    private var _data: List<ICell> = emptyList()

    fun updateData(newData: List<ICell>) {
        _data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainVH.create(parent)

    override fun onBindViewHolder(holder: MainVH, position: Int) = holder.bind(_data[position])

    override fun getItemCount() = _data.size
}