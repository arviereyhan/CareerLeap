package com.example.carrerleap.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.carrerleap.R
import com.example.carrerleap.data.dummy.QuestionDataSource
import com.example.carrerleap.databinding.ActivityMainBinding
import com.example.carrerleap.ui.question.QuestionActivity
import com.example.carrerleap.utils.JobsModel
import com.example.carrerleap.utils.Preferences

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: Preferences
    private lateinit var jobsModel: JobsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)

        var score = preferences.getJobs().score
        var jobs = preferences.getJobs().jobsId
        var size = score?.size!! * 5

        Log.d("size", size.toString())

        Log.d("jobs", jobs!!.toString())

        val total = score?.sum()?.toFloat() // Jumlahkan semua nilai dan ubah ke tipe Float
        val combinedPercentage = (total!! / size) * 100// Bagi jumlah persentase dengan jumlah elemen
        Log.d("percentages", combinedPercentage.toString())

        printIntArray(score!!)
    }


    private fun printIntArray(array: IntArray) {
        val arrayString = array.joinToString(", ")
        Log.d("Array Log", "Array: $arrayString")
    }
}