package com.glivion.plasticdiary.view.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.MilestoneItemLayoutBinding
import com.glivion.plasticdiary.model.profile.Milestone
import timber.log.Timber

class MilestoneAdapter :
    ListAdapter<Milestone, MilestoneAdapter.MilestoneViewHolder>(DIFF_UTIL_CALLBACK) {

    class MilestoneViewHolder(val binding: MilestoneItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_milestone: Milestone) {
            val progress = (_milestone.completedPoints.toFloat() / _milestone.points.toFloat()) * 100
            Timber.e("points: $progress")
            binding.apply {
                milestone = _milestone
                progressBar.progress = progress.toInt()
            }
            if (_milestone.completedPoints >= _milestone.points) {
                binding.apply {
                    remainingPoints.visibility = View.GONE
                    milestoneCompleted.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Milestone>() {
            override fun areItemsTheSame(oldItem: Milestone, newItem: Milestone): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Milestone, newItem: Milestone): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val binding: MilestoneItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.milestone_item_layout, parent, false
        )
        return MilestoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        val milestone = getItem(position)
        holder.bind(milestone)
    }

}