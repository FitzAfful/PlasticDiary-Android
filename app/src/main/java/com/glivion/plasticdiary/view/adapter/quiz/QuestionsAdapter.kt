package com.glivion.plasticdiary.view.adapter.quiz

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.QuestionDetailLayoutItemBinding
import com.glivion.plasticdiary.model.questions.Question
import timber.log.Timber

class QuestionsAdapter(val context: Context,
data: ArrayList<Question>): RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    init {
        questionsList = data
    }
    class QuestionViewHolder(private val binding: QuestionDetailLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_question: Question, context: Context) {
            binding.apply {
                question = _question
                questionNumber.text = "Question ${(adapterPosition + 1)}"
                binding.optionA.text = _question.option1
                binding.optionB.text = _question.option2
                binding.optionC.text = _question.option3
                binding.optionD.text = _question.option4
                when (_question.correct) {
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
                        optionB.setTextColor( ColorStateList.valueOf(
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
                }
            }
        }

    }

    companion object {
         private var questionsList = ArrayList<Question>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding: QuestionDetailLayoutItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.question_detail_layout_item, parent, false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionsList[position]
        holder.bind(question, context)
    }

    override fun getItemCount(): Int = questionsList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setUpItems(questions: ArrayList<Question>) {
        questionsList = questions
        notifyDataSetChanged()
    }
}