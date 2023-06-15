package com.example.carrerleap.ui.homescreen.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.R
import com.example.carrerleap.data.userdata.UserData
import com.example.carrerleap.databinding.FragmentProfileBinding
import com.example.carrerleap.ui.auth.login.LoginViewModel
import com.example.carrerleap.ui.homescreen.ui.editprofile.EditProfileActivity
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import com.bumptech.glide.Glide
import com.example.carrerleap.ui.auth.LoginRegisterActivity
import com.example.carrerleap.utils.Job
import java.io.File


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private var jobs: List<Job>? = null
    private var relatedJobs: List<Job>? = null
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private  lateinit var token: String
    private lateinit var profileViewModel: ProfileViewModel
    private  lateinit var userData: UserData
    private var isLoaded: Boolean = false
    private  var outputDateString: String? = null





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fullScreen()

        preferences = Preferences(requireContext())

        userModel = preferences.getToken() //ambil token untuk get_profile
        token = userModel.token!!

        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        profileViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[ProfileViewModel::class.java]

        setupview()

        binding.editButton1.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            if(isLoaded){               //mengecek apakah data sudah diload dari server, karena jika pindah activity masih belum keload nanti kena NPE
                intent.putExtra("EXTRA_DATA",userData)
                startActivity(intent)
            }

        }

        binding.Logoutbutton.setOnClickListener {
            preferences.logout()
            val intent = Intent(context, LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return root
    }

    private fun fullScreen() {
        requireActivity().window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        requireActivity().actionBar?.hide()
    }


    override fun onResume() {
        super.onResume()              // Agar data tetap up-to-date ketika kembali dari edit profile activity
        setupview()
     }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupview(){
        profileViewModel.get_profile(token).observe(viewLifecycleOwner){
            when(it) {
                is Result.Success -> {

                    if(it.data.userProfile?.fullName == null){
                        binding.textViewName.text = getString(R.string.not_available)
                    }
                    else {
                        binding.textViewName.text = it.data.userProfile?.fullName
                    }

                    if(it.data.userProfile?.dateOfBirth == null){
                        binding.textViewBirth.text = "Date of birth is empty"
                    }
                    else {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd-MM-YYYY", Locale.getDefault())
                        val inputDate: Date = inputFormat.parse(it.data.userProfile?.dateOfBirth)
                        outputDateString = outputFormat.format(inputDate)
                        binding.textViewBirth.text = outputDateString
                    }

                    if(it.data.userProfile?.email == null){
                        binding.textViewEmail.text = "email is empty"
                        Log.i("userdata","1")

                    }
                    else{
                        binding.textViewEmail.text = it.data.userProfile?.email
                    }

                    if(it.data.userProfile?.location == null){
                        binding.textViewLocation.text = "Location is empty"
                    }
                    else {
                        binding.textViewLocation.text = it.data.userProfile?.location
                    }

                    if(it.data.userProfile?.phoneNumber== null){
                        binding.textViewPhonenumber.text = "Phone number is empty"
                    }
                    else {
                        binding.textViewPhonenumber.text = it.data.userProfile?.phoneNumber
                    }

                    if(it.data.userProfile?.jobId== null){
                        binding.textViewCareer.text = "Career is empty"
                    }
                    else {
                        getJobs(it.data.userProfile?.jobId)
                    }
                    Log.i("kinerja","1")



                    if(it.data.userProfile?.profileUrl==null){
                        it.data.userProfile?.profileUrl=getString(R.string.default_pp)
                        Log.i("kinerja","2")
                    }

                    Glide.with(this)
                        .load(it.data.userProfile?.profileUrl)
                        .into(binding.profileImage)



                    userData = UserData(it.data.userProfile?.fullName,
                        it.data.userProfile?.profileUrl,
                        outputDateString,
                        it.data.userProfile?.phoneNumber,
                        it.data.userProfile?.email,
                        it.data.userProfile?.location,
                        it.data.userProfile?.jobId.toString()
                    )
                    isLoaded = true
                    Log.i("isloaded",isLoaded.toString())
                    Log.i("kinerja","3")



                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getJobs(jobId: Int) {
        profileViewModel.getJobs(token).observe(viewLifecycleOwner) { result ->
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
                    binding.textViewCareer.text = jobNames
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}