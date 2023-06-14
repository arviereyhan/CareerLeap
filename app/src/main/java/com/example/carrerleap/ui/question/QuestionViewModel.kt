package com.example.carrerleap.ui.question

import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class QuestionViewModel(private val repository: DataRepository): ViewModel() {
    fun getQuestions(token: String) = repository.getQuestions(token)
    fun postScore(id: Int, score: Int, token: String) = repository.postScore(id, score, token)
    fun getProfile(token: String) = repository.getProfile(token)

    fun getScore(token: String) = repository.getHome(token)
}