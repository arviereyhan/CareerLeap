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


    }
}