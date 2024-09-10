package com.gogote.hscmcq

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogote.hscmcq.databinding.QuizItemRecyclerRowBinding

class QuizListAdpater(private val quizList: List<QuizModel>) :
    RecyclerView.Adapter<QuizListAdpater.MyViewHolder>() {

    class MyViewHolder(private val binding: QuizItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind (model: QuizModel){
            // bind all the views
            binding.apply {
                quizTitle.text = model.title
                quizSubtitle.text = model.subtitle
                quizTime.text = model.time + " min"
                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java)
                    QuizActivity.questionsModelList = model.questionsList
                    QuizActivity.time = model.time
                    root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizList[position])
    }
}