package com.example.carrerleap.ui.question

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.databinding.ActivityQuestionBinding
import com.example.carrerleap.ui.homescreen.HomeScreenActivity
import com.example.carrerleap.utils.JobsModel
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Question
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding
    private var currentQuestionIndex = 0
    private var questions: List<Question>? = null
    private val userAnswers: ArrayList<Int> = ArrayList()
    private lateinit var preferences: Preferences
    private lateinit var jobsModel: JobsModel
    private lateinit var userModel: UserModel
    private lateinit var viewModel: QuestionViewModel
    private var token: String = ""
    private var jobsId : Int = 0
    private var selectedOptionId: Int = 0
    private var relatedQuestions: List<Question>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this@QuestionActivity,
            viewModelFactory
        )[QuestionViewModel::class.java]
        setupView()
        preferences = Preferences(this)

        jobsModel = preferences.getJobs()
        userModel = preferences.getToken()
        token = userModel.token.toString()

        viewModel.getProfile(token).observe(this){
            when(it){
                is Result.Success -> {
                    if (it.data.userProfile?.jobId != null){
                        jobsId = it.data.userProfile.jobId
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
            selectedOptionId = jobsId
        }


        Log.i("test", selectedOptionId.toString())

        preferences = Preferences(this)
        viewModel.getQuestions(token).observe(this) {
            when (it) {
                is Result.Success -> {
                    val questionResponse = it.data
                    questions = questionResponse.data?.map {
                        Question(
                            id = it?.id!!,
                            question = it.question!!,
                            jobId = it.jobId!!
                        )
                    }
                    if (selectedOptionId == 0) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (selectedOptionId != null) {
                                relatedQuestions = questions?.filter { it.jobId == selectedOptionId }
                                showQuestion(currentQuestionIndex)
                            } else {
                                // Pekerjaan yang dipilih tidak ditemukan
                            }
                        }, 2000)
                    } else {
                        if (selectedOptionId != null) {
                            relatedQuestions = questions?.filter { it.jobId == selectedOptionId }
                            showQuestion(currentQuestionIndex)
                        } else {
                            // Pekerjaan yang dipilih tidak ditemukan
                        }
                    }
                }

                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        jobsModel = preferences.getJobs()

        questionHandler()


        binding.btnNext.setOnClickListener {
            val selectedRadioButtonId: Int = binding.radioQuestion.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
                val answerValue: Int = getAnswerValue(selectedRadioButton.text.toString())
                val currentQuestionId = relatedQuestions?.get(currentQuestionIndex)?.id ?: 0

                userAnswers.add(answerValue)
                viewModel.postScore(currentQuestionId, answerValue, token ).observe(this){
                    when (it) {
                        is Result.Success -> {
                            showLoading(true)
                            currentQuestionIndex++

                            if (currentQuestionIndex < relatedQuestions?.size ?: 0) {
                                showQuestion(currentQuestionIndex)
                                showLoading(false)
                            } else {
                                navigateToHomePage()
                            }
                        }
                        is Result.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                Toast.makeText(this, "Pilih salah satu pilihan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.loadingState.visibility = View.VISIBLE
        } else {
            binding.loadingState.visibility = View.GONE
        }
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun showQuestion(index: Int) {
        val question = relatedQuestions?.get(index)?.question
        binding.tvQuestion.text = question
        binding.radioQuestion.clearCheck()
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.putExtra(USER_ANSWERS, userAnswers.toIntArray())
        val mainModel = JobsModel(
            score = userAnswers.toIntArray(),
            jobsId = selectedOptionId
        )
        preferences.saveJobs(mainModel)
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

    private fun questionHandler() {
        viewModel.getScore(token).observe(this){
            when (it) {
                is Result.Success -> {
                    val data = it.data.data
                    if (data != null) {
                        startActivity(Intent(this, HomeScreenActivity::class.java).also {
                            finish()
                        })
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val JOBS = "jobs"
        const val JOBS_ID = "jobs_id"
        const val USER_ANSWERS = "user_answers"
    }
}