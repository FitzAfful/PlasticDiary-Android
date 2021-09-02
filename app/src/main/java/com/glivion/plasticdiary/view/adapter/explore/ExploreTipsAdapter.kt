package com.glivion.plasticdiary.view.adapter.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.TipsItemLayoutBinding
import com.glivion.plasticdiary.model.explore.Tip

abstract class ExploreTipsAdapter: ListAdapter<Tip, ExploreTipsAdapter.TipViewHolder>(DIFF_UTIL_CALLBACK) {

    class TipViewHolder(private val binding: TipsItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tip: Tip, adapter: ExploreTipsAdapter) {
            binding.apply {
                tips = tip
                viewTip.setOnClickListener {
                    adapter.viewTip(tip)
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Tip>() {
            override fun areItemsTheSame(oldItem: Tip, newItem: Tip): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Tip, newItem: Tip): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val binding: TipsItemLayoutBinding = DataBindingUtil.inflate( LayoutInflater.from(parent.context),
            R.layout.tips_item_layout,
            parent,
            false)

        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val tip = getItem(position)
        holder.bind(tip, this)
    }

    abstract fun viewTip(tip: Tip)
}