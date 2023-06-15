package com.example.carrerleap.ui.homescreen.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carrerleap.databinding.ItemRowHomeBinding
import com.example.carrerleap.utils.ListHomeItem
import com.example.carrerleap.utils.Question

class HomeAdapter : ListAdapter<ListHomeItem, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {
    private var recommendationClickListener: ((ListHomeItem) -> Unit)? = null

    fun setRecommendationClickListener(listener: (ListHomeItem) -> Unit) {
        recommendationClickListener = listener
    }

    private var relatedQuestions: List<Question>? = null

    fun updateRelatedQuestions(questions: List<Question>?) {
        relatedQuestions = questions
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataTwo: Question,data: ListHomeItem, listener: ((ListHomeItem) -> Unit)?) {
            binding.apply {
                tvQuestion.text = dataTwo.question
                tvScore.text = data.skill

                btnRecommendation.setOnClickListener {
                    listener?.invoke(data)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListHomeItem>() {
            override fun areItemsTheSame(oldItem: ListHomeItem, newItem: ListHomeItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListHomeItem,
                newItem: ListHomeItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = relatedQuestions?.get(position)
        val data2 = getItem(position)
        if (data != null) {
            holder.bind(data,data2 , recommendationClickListener)
        }
    }

    override fun getItemCount(): Int {
        return relatedQuestions?.size ?: 0
    }
}
