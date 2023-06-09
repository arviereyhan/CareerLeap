package com.example.carrerleap.ui.homescreen.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.data.userdata.UserData
import com.example.carrerleap.databinding.FragmentProfileBinding
import com.example.carrerleap.ui.auth.login.LoginViewModel
import com.example.carrerleap.ui.homescreen.ui.editprofile.EditProfileActivity
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private  lateinit var token: String
    private lateinit var profileViewModel: ProfileViewModel
    private  lateinit var userData: UserData





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
            intent.putExtra("EXTRA_DATA",userData)
            startActivity(intent)
        }

        return root
    }


    //override fun onResume() {
        //super.onResume()              // KEMUNGKINAN JIKA DATA PADA PROFILE TIDAK UP TO DATE SETELAH KEMBALI DARI UPDATE
        //setupview()
    // }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupview(){
        profileViewModel.get_profile(token).observe(viewLifecycleOwner){
            when(it) {
                is Result.Success -> {

                    if(it.data.userProfile?.fullName == null){
                        binding.textViewName.text = "data kosong"
                    }
                    else {
                        binding.textViewName.text = it.data.userProfile?.fullName
                    }

                    if(it.data.userProfile?.dateOfBirth == null){
                        binding.textViewBirth.text = "data kosong"
                    }
                    else {
                        binding.textViewBirth.text = it.data.userProfile?.dateOfBirth
                    }

                    if(it.data.userProfile?.email == null){
                        binding.textViewEmail.text = "data kosong"
                    }
                    else{
                        binding.textViewEmail.text = it.data.userProfile?.email
                    }

                    if(it.data.userProfile?.location == null){
                        binding.textViewLocation.text = "data kosong"
                    }
                    else {
                        binding.textViewLocation.text = it.data.userProfile?.location
                    }

                    if(it.data.userProfile?.phoneNumber== null){
                        binding.textViewPhonenumber.text = "data kosong"
                    }
                    else {
                        binding.textViewPhonenumber.text = it.data.userProfile?.phoneNumber
                    }

                    userData = UserData(it.data.userProfile?.fullName,
                        "dummy profile url",
                        it.data.userProfile?.dateOfBirth,
                        it.data.userProfile?.phoneNumber,
                        it.data.userProfile?.email,
                        it.data.userProfile?.location
                    )


                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}