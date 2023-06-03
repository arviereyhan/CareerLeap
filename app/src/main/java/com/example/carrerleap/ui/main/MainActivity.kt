package com.example.carrerleap.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.carrerleap.R
import com.example.carrerleap.ui.question.QuestionActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var score = intent.getIntArrayExtra(QuestionActivity.USER_ANSWERS)
        printIntArray(score!!)
    }

    private fun printIntArray(array: IntArray) {
        val arrayString = array.joinToString(", ")
        Log.d("Array Log", "Array: $arrayString")
    }
}