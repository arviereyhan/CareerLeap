package com.example.carrerleap.ui.question

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.TextView
import com.example.carrerleap.R
import com.example.carrerleap.data.dummy.QuestionDataSource
import com.example.carrerleap.databinding.ActivityQuestionBinding
import com.example.carrerleap.ui.main.MainActivity

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding
    private var currentQuestionIndex = 0
    private lateinit var question: List<String>
    private val userAnswers: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedOption = intent.getStringExtra(JOBS).toString()
        Log.i("test", selectedOption)
        question = QuestionDataSource.getQuestions(selectedOption)!!
        binding.tvQuestion.text = question[currentQuestionIndex]


        binding.btnNext.setOnClickListener {
            val selectedRadioButtonId: Int = binding.radioQuestion.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
                val answerValue: Int = getAnswerValue(selectedRadioButton.text.toString())

                userAnswers.add(answerValue)

                currentQuestionIndex++

                if (currentQuestionIndex < question.size) {
                    val nextQuestion = question[currentQuestionIndex]
                    showQuestion(nextQuestion)
                } else {
                    navigateToHomePage()
                }
            } else {
                // Tidak ada RadioButton yang dipilih, lakukan penanganan sesuai kebutuhan Anda
            }
        }
    }

    private fun showQuestion(question: String) {
        // Tampilkan pertanyaan pada TextView atau tampilan yang sesuai
        binding.tvQuestion.text = question
        binding.radioQuestion.clearCheck()
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USER_ANSWERS, userAnswers.toIntArray())
        startActivity(intent)
        finish()

        printIntArray(userAnswers.toIntArray())
    }

    private fun getAnswerValue(answer: String): Int {
        return when (answer) {
            "Tidak bisa sama sekali" -> 1
            "Hanya sekedar mengetahui" -> 2
            "Sedikit menguasai" -> 3
            "Lumayan Menguasai" -> 4
            "Sangat menguasai" -> 5
            else -> 0 // Nilai default jika tidak ada pilihan yang cocok
        }
    }

    private fun printIntArray(array: IntArray) {
        val arrayString = array.joinToString(", ")
        Log.d("Array Log", "Array: $arrayString")
    }

    companion object{
        const val JOBS = "jobs"
        const val USER_ANSWERS = "user_answers"
    }
}