package com.glivion.plasticdiary.view.adapter.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.QuizTypeItemLayoutBinding
import com.glivion.plasticdiary.model.quiz.QuizInterface

abstract class QuizPageAdapter(val type: String) :
    ListAdapter<QuizInterface, QuizPageAdapter.QuizPageViewHolder>(
        DIFF_UTIL_CALLBACK
    ) {

     class QuizPageViewHolder(private val binding: QuizTypeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_category: QuizInterface, type: String, adapter: QuizPageAdapter) {
            if (type == "category") {
                binding.apply {
                    viewResults.visibility = View.GONE
                    scoreLyt.visibility = View.GONE
                    start.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    viewResults.visibility = View.VISIBLE
                    scoreLyt.visibility = View.VISIBLE
                    start.visibility = View.GONE
                }
            }
            binding.apply {
                category = _category
                start.setOnClickListener {
                    adapter.startQuiz(_category)
                }
                viewResults.setOnClickListener {
                    adapter.viewResults(_category)
                }
            }

            binding.executePendingBindings()
        }
    }


    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<QuizInterface>() {
            override fun areItemsTheSame(oldItem: QuizInterface, newItem: QuizInterface): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: QuizInterface, newItem: QuizInterface): Boolean =
                oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizPageViewHolder {
        val binding: QuizTypeItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.quiz_type_item_layout, parent, false
        )
        return QuizPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizPageViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category, type, this)
    }

    abstract fun startQuiz(category: QuizInterface)

    abstract fun viewResults(category: QuizInterface)
}