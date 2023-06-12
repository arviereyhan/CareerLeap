package com.example.carrerleap.ui.choose

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.R
import com.example.carrerleap.databinding.ActivityChooseBinding
import com.example.carrerleap.ui.question.QuestionActivity
import com.example.carrerleap.utils.JobsModel
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory

class ChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseBinding
    private var selectedItem: String? = null
    private lateinit var preferences: Preferences
    private lateinit var jobsModel: JobsModel
    private lateinit var userModel: UserModel
    private var token: String = ""
    private lateinit var viewModel: ChooseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this@ChooseActivity,
            viewModelFactory
        )[ChooseViewModel::class.java]
        preferences = Preferences(this)

        jobsModel = preferences.getJobs()
        userModel = preferences.getToken()
        token = userModel.token.toString()

        questionHandler()
        jobs()
    }

    private fun jobs(){
        viewModel.getJobs(token).observe(this){response ->
            when(response){
                is Result.Success -> {
                    val data = response.data
                    val jobs = data.data.map { it.job_name }
                    showJobs(jobs)
                    binding.btnToQuestion.setOnClickListener {
                        if (selectedItem != null){
                            val selectedJob = selectedItem // Pekerjaan yang dipilih dari dropdown
                            val selectedJobId = data.data.find { it.job_name == selectedJob }?.id
                            val intent = Intent(this@ChooseActivity, QuestionActivity::class.java)
                            val mainModel = JobsModel(
                                jobsId = selectedJobId
                            )
                            preferences.saveJobs(mainModel)
                            viewModel.postJobs(selectedJobId!!.toInt(), token).observe(this){
                                when(it){
                                    is Result.Success -> {
                                        Toast.makeText(this, "Anda memilih: $selectedItem", Toast.LENGTH_SHORT).show()
                                    }
                                    is Result.Error -> {
                                        Toast.makeText(this, it.error , Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            intent.putExtra(QuestionActivity.JOBS, selectedItem)
                            intent.putExtra(QuestionActivity.JOBS_ID, selectedJobId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Silakan pilih item dari dropdown", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showJobs(jobs: List<String>) {
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, jobs)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selectedItem = parent.getItemAtPosition(position) as String
            }
    }

    private fun questionHandler(){
        viewModel.getProfile(token).observe(this){
            when(it){
                is Result.Success -> {
                    if (it.data.userProfile?.jobId != null){
                        startActivity(Intent(this, QuestionActivity::class.java).also {
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
}