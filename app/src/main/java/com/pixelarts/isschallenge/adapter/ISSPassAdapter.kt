package com.pixelarts.isschallenge.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pixelarts.isschallenge.R
import com.pixelarts.isschallenge.databinding.RvLayoutBinding
import com.pixelarts.isschallenge.model.ISSPassData

class ISSPassAdapter(private val issPassDataList: List<ISSPassData>) : RecyclerView.Adapter<ISSPassAdapter.ViewHolder>(){

    private lateinit var context:Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        context = viewGroup.context
        val binding = DataBindingUtil.inflate<RvLayoutBinding>(LayoutInflater.from(context), R.layout.rv_layout, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = issPassDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = issPassDataList[position]
            holder.binder.apply {
                tvDuration.text = data.duration
                tvTimeStamp.text = data.timeStamp
            }
    }

    class ViewHolder(var binder: RvLayoutBinding): RecyclerView.ViewHolder(binder.root)
}