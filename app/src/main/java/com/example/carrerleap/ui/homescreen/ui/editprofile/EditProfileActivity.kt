package com.example.carrerleap.ui.homescreen.ui.editprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.carrerleap.R
import com.example.carrerleap.data.userdata.UserData
import com.example.carrerleap.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var userData: UserData
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getParcelableExtra("EXTRA_DATA")!!

        setupView()





    }

    private fun setupView() {
        if (userData.name==null){
            binding.textViewName.text = getString(R.string.not_available)
        }
        else{
            binding.textViewName.text = userData.name
        }


        if (userData.birthdate==null){
            binding.birthdate.text = getString(R.string.not_available)
        }
        else{
            binding.birthdate.text = userData.birthdate
        }

        if (userData.email==null){
            binding.email.text = getString(R.string.not_available)
        }
        else{
            binding.email.text = userData.email
        }

        if (userData.location==null){
            binding.location.text = getString(R.string.not_available)
        }
        else{
            binding.location.text = userData.location
        }
        if (userData.phonenumber==null){
            binding.phonenumber.text = getString(R.string.not_available)
        }
        else{
            binding.phonenumber.text = userData.phonenumber
        }


    }
}