package com.glivion.plasticdiary.view.adapter.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.LeaderBoardItemLayoutBinding
import com.glivion.plasticdiary.databinding.LeaderboardHeaderLayoutBinding
import com.glivion.plasticdiary.model.profile.LeaderboardInterface

class LeaderboardAdapter: ListAdapter<LeaderboardInterface, RecyclerView.ViewHolder>(DIFF_UTIL_CALLBACK) {
    private val HEADER_VIEW = 1
    private val LIST_VIEW = 2

    class HeaderViewHolder(val binding: LeaderboardHeaderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_leaderboard: LeaderboardInterface, position: Int) {
            binding.apply {
                leaderboard = _leaderboard
                userPosition.text = (adapterPosition + 1).toString()
            }
        }
    }

    class ListItemViewHolder(val binding: LeaderBoardItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_leaderboard: LeaderboardInterface, position: Int) {
            binding.apply {
                leaderboard = _leaderboard
                userPosition.text = (adapterPosition + 1).toString()
            }
        }
    }


    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<LeaderboardInterface>() {
            override fun areItemsTheSame(oldItem: LeaderboardInterface, newItem: LeaderboardInterface): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: LeaderboardInterface, newItem: LeaderboardInterface): Boolean =
                oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            HEADER_VIEW -> {
                val binding: LeaderboardHeaderLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.leaderboard_header_layout,
                    parent,
                    false
                )
                viewHolder = HeaderViewHolder(binding)
            }
            LIST_VIEW -> {
                val binding: LeaderBoardItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.leader_board_item_layout,
                    parent,
                    false
                )
                viewHolder = ListItemViewHolder(binding)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val leaderboard = getItem(position)
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(leaderboard, position)
            }
            is ListItemViewHolder -> {
                holder.bind(leaderboard, position)
            }
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_VIEW
        } else {
            LIST_VIEW
        }
    }
}