package com.example.carrerleap.ui.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carrerleap.data.remote.response.CourseItem
import com.example.carrerleap.databinding.ItemRowCourseBinding

class CourseAdapter(private val listCourse: List<CourseItem>): RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    private var courseClickListener: ((CourseItem) -> Unit)? = null

    fun setCourseCoClickListener(listener: (CourseItem) -> Unit) {
        courseClickListener = listener
    }


    inner class ViewHolder(private val binding: ItemRowCourseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: CourseItem, listener: ((CourseItem) -> Unit)?){

            binding.tvTitleCourse.text = story.courseTitle
            binding.tvCourseDescription.text = story.courseDescription

            binding.btnCourse.setOnClickListener {
                listener?.invoke(story)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseAdapter.ViewHolder {
        val view = ItemRowCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder((view))
    }

    override fun getItemCount() = listCourse.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCourse[position], courseClickListener)
    }
}