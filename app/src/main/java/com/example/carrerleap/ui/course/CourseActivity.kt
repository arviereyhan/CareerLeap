package com.example.carrerleap.ui.course

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrerleap.R
import com.example.carrerleap.data.remote.response.CourseItem
import com.example.carrerleap.databinding.ActivityCourseBinding
import com.example.carrerleap.ui.choose.ChooseViewModel
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory

class CourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseBinding
    private lateinit var viewModel: CourseViewModel
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private lateinit var layoutManager: LinearLayoutManager
    private var token: String = ""
    private var questionsId: Int = 0
    private var score: Int = 0
    private var jobsId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Rekomendasi Kami"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this@CourseActivity,
            viewModelFactory
        )[CourseViewModel::class.java]
        layoutManager = LinearLayoutManager(this@CourseActivity)
        binding.rvCourse.layoutManager = layoutManager
        binding.rvCourse.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvCourse.addItemDecoration(itemDecoration)
        preferences = Preferences(this)

        userModel = preferences.getToken()
        token = userModel.token.toString()

        questionsId = intent.getIntExtra(QUESTION_ID, 0)
        score = intent.getIntExtra(USER_SCORE, 0)
        jobsId = intent.getIntExtra(JOBS_ID, 0)
        Log.d("test quest", questionsId.toString())
        Log.d("test score", score.toString())
        Log.d("test jobs", jobsId.toString())

        getCourse()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Kembali ke HomeFragment
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun getCourse(){
        viewModel.getCourse(questionsId, score, jobsId, token).observe(this){
            when (it){
                is Result.Success -> {
                    val adapter = CourseAdapter(it.data.courseItem)
                    binding.rvCourse.adapter = adapter
                    adapter.setCourseCoClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(it.courseLink)
                        startActivity(intent)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        const val QUESTION_ID = "question_id"
        const val USER_SCORE = "user_score"
        const val JOBS_ID = "jobs_id"
    }
}