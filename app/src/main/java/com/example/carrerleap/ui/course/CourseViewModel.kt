package com.example.carrerleap.ui.course

import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class CourseViewModel(private val repository: DataRepository):ViewModel() {
    fun getCourse(questionId: Int, userSCore: Int, jobId: Int, token: String) = repository.getCourse(questionId, userSCore, jobId, token)
}