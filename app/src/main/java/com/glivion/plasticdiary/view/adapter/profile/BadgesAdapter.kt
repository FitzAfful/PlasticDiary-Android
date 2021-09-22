package com.glivion.plasticdiary.view.adapter.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.BadgeItemLayoutBinding
import com.glivion.plasticdiary.model.profile.Badges

abstract class BadgesAdapter : ListAdapter<Badges, BadgesAdapter.BadgesViewHolder>(DIFF_UTIL_CALLBACK) {

    class BadgesViewHolder(val binding: BadgeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_badges: Badges, adapter: BadgesAdapter) {
            binding.apply {
                badges = _badges
                binding.root.setOnClickListener {
                    adapter.viewRewardDetails(_badges)
                }
            }
        }
    }

    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Badges>() {
            override fun areItemsTheSame(oldItem: Badges, newItem: Badges): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Badges, newItem: Badges): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgesViewHolder {
        val binding: BadgeItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.badge_item_layout, parent, false
        )
        return BadgesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgesViewHolder, position: Int) {
        val badges = getItem(position)
        holder.bind(badges, this)
    }

    abstract fun viewRewardDetails(badges: Badges)
}