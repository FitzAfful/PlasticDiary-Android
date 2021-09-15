package com.glivion.plasticdiary.view.adapter.quiz

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.QuestionDetailLayoutItemBinding
import com.glivion.plasticdiary.model.questions.Question

class QuestionsAdapter(val context: Context): ListAdapter<Question, QuestionsAdapter.QuestionViewHolder>(
    DIFF_UTIL_CALLBACK) {

    class QuestionViewHolder(private val binding: QuestionDetailLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_question: Question, context: Context) {
            binding.apply {
                question = _question
                questionNumber.text = "Question ${(adapterPosition + 1)}"


               /* when (_question.correct) {
                    optionA.text.toString() -> {
                        a.setTextColor(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.heading_text_green
                                )
                            ))
                        optionA.setTextColor( ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.heading_text_green
                            )
                        ))
                    }
                    optionB.text.toString() -> {
                        b.setTextColor( ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.heading_text_green
                            )
                        ))
                        optionA.setTextColor( ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.heading_text_green
                            )
                        ))
                    }
                    optionC.text.toString() -> {
                        c.setTextColor( ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.heading_text_green
                            )
                        ))
                        optionC.setTextColor( ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.heading_text_green
                            )
                        ))
                    }
                    optionD.text.toString() -> {
                        Timber.e("answer: ${_question.correct} selection: ${optionD.text}")
                        d.setTextColor( ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.heading_text_green
                            )
                        ))
                        optionD.setTextColor( ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.heading_text_green
                            )
                        ))
                    }
                }*/
            }
        }

    }

    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding: QuestionDetailLayoutItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.question_detail_layout_item, parent, false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question, context)
    }
}