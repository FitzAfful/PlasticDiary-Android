package com.glivion.plasticdiary.view.adapter.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ExploreResourceItemLayoutBinding
import com.glivion.plasticdiary.model.explore.Resource

abstract class ExploreResourcesAdapter: ListAdapter<Resource, ExploreResourcesAdapter.ResourceViewHolder>(
    DIFF_UTIL_CALLBACK) {

    class ResourceViewHolder(private val binding: ExploreResourceItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resource: Resource, adapter: ExploreResourcesAdapter) {
            binding.apply {
                resources = resource
                visit.setOnClickListener {
                    adapter.visitLink(resource)
                }
            }
            binding.executePendingBindings()
        }
    }
    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Resource>() {
            override fun areItemsTheSame(oldItem: Resource, newItem: Resource): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Resource, newItem: Resource): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val binding: ExploreResourceItemLayoutBinding = DataBindingUtil.inflate( LayoutInflater.from(parent.context),
            R.layout.explore_resource_item_layout,
            parent,
            false)

        return ResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val resource = getItem(position)
        holder.bind(resource, this)
    }

    abstract fun visitLink(resource: Resource)

}