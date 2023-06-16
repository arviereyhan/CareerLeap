package com.example.carrerleap.ui.homescreen.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.carrerleap.databinding.FragmentHomeBinding
import com.example.carrerleap.ui.course.CourseActivity
import com.example.carrerleap.utils.Job
import com.example.carrerleap.utils.ListHomeItem
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Question
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var preferences: Preferences
    private var jobs: List<Job>? = null
    private val userAnswers: ArrayList<Int> = ArrayList()
    private var userId: Int = 0
    private var selectionJobsId: Int = 0
    private lateinit var userModel: UserModel
    private var token: String = ""
    private lateinit var layoutManager: LinearLayoutManager
    private var relatedJobs: List<Job>? = null
    private lateinit var homeAdapter: HomeAdapter
    private var selectionUserId : Int = 0
    private var relatedQuestions: List<Question>? = null
    private var questions: List<Question>? = null
    private var questionsId: Int = 0
    private var userScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupView()

        preferences = Preferences(requireContext())
        layoutManager = LinearLayoutManager(requireContext())
        binding.rvHome.layoutManager = layoutManager
        binding.rvHome.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvHome.addItemDecoration(itemDecoration)

        userModel = preferences.getToken()
        token = userModel.token.toString()

        homeAdapter = HomeAdapter()

        getProfile()
        if (selectionJobsId == 0){
            Handler(Looper.getMainLooper()).postDelayed({
                Log.d("selection user Id", selectionJobsId.toString())
                setRecycleView()
            }, 3000)
        }else {
            setRecycleView()
        }

        return root
    }



    private fun printIntArray(array: IntArray) {
        val arrayString = array.joinToString(", ")
        Log.d("Array Log", "Array: $arrayString")
    }

    private fun setRecycleView(){
        viewModel.getQuestion(token).observe(viewLifecycleOwner){
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
                    if (selectionJobsId != null) {
                        relatedQuestions = questions?.filter { it.jobId == selectionJobsId }
                        homeAdapter.updateRelatedQuestions(relatedQuestions)
                        getHomeData()

                    } else {
                        // Pekerjaan yang dipilih tidak ditemukan
                    }
                }

                is Result.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupView() {
        requireActivity().window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        requireActivity().actionBar?.hide()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.loadingState.visibility = View.VISIBLE
        } else {
            binding.loadingState.visibility = View.GONE
        }
    }

    private fun getHomeData() {
        viewModel.getHome(token).observe(viewLifecycleOwner) { homeResult ->
            when (homeResult) {
                is Result.Success -> {
                    val data = homeResult.data
                    val scoreList = data.data?.mapNotNull { scoreItem ->
                        val score = scoreItem?.score
                        userScore = scoreItem.score
                        userAnswers.add(score!!)
                        val skill = when (score) {
                            1 -> "Tidak bisa sama sekali"
                            2 -> "Hanya sekedar mengetahui"
                            3 -> "Sedikit menguasai"
                            4 -> "Lumayan Menguasai"
                            5 -> "Sangat menguasai"
                            else -> null
                        }
                        if (skill != null) {
                            ListHomeItem(scoreItem?.id!!.toInt(), skill,scoreItem?.questionId!!.toInt(), score )
                        } else {
                            null
                        }

                    }

                    homeAdapter.submitList(scoreList)
                    showLoading(false)
                    homeAdapter.setRecommendationClickListener {
                        val intent = Intent(activity, CourseActivity::class.java)
                        intent.putExtra(CourseActivity.QUESTION_ID, it.questionId)
                        intent.putExtra(CourseActivity.USER_SCORE, it.score)
                        intent.putExtra(CourseActivity.JOBS_ID, selectionJobsId)
                        requireActivity().startActivity(intent)
                    }

                    binding.rvHome.adapter = homeAdapter
                    var score = userAnswers.toIntArray()
                    var size = score.size * 5
                    Log.d("size", size.toString())
                    val total = score.sum().toFloat() // Jumlahkan semua nilai dan ubah ke tipe Float
                    val combinedPercentage = (total / size) * 100// Bagi jumlah persentase dengan jumlah elemen
                    Log.d("percentages", combinedPercentage.toString())

                    printIntArray(score)

                    binding.progressBar.progress = combinedPercentage.toInt()
                    binding.progressBar.max = 100
                    binding.tvProgressPercent.text = String.format("%.0f%%", combinedPercentage)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), homeResult.error, Toast.LENGTH_SHORT).show()
                    Log.i("tastos", homeResult.error)
                }
            }
        }
    }

    private fun getProfile() {
        viewModel.getProfile(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(true)
                    val tempImg =
                        "https://storage.googleapis.com/career-leap/careerLeap-default-profilePicture-9%20June%202023.png"
                    val data = result.data
                    userId = data.userProfile?.id!!
                    selectionUserId = userId
                    if (data.userProfile?.profileUrl != null) {
                        Glide.with(this).load(data.userProfile.profileUrl)
                            .into(binding.imgProfile)
                    } else {
                        Glide.with(this).load(tempImg).into(binding.imgProfile)
                    }
                    binding.tvNameUser.text = data.userProfile?.fullName
                    binding.tvLocation.text = data.userProfile?.location ?: "belum ada lokasi"

                    val jobId = data.userProfile?.jobId
                    selectionJobsId = jobId!!.toInt()
                    getJobs(jobId)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getJobs(jobId: Int) {
        viewModel.getJobs(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val dataJob = result.data
                    jobs = dataJob.data.map {
                        Job(
                            id = it.id,
                            jobName = it.job_name
                        )
                    }
                    relatedJobs = jobs?.filter { it.id == jobId }
                    val jobNames = relatedJobs?.joinToString(", ") { it.jobName }
                    binding.tvJobs.text = jobNames
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}