package com.example.carrerleap.ui.uploadcv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.carrerleap.R
import com.example.carrerleap.databinding.ActivityUploadCvBinding
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.UserModel

class UploadCvActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private var token: String = ""
    private lateinit var binding: ActivityUploadCvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadCvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = Preferences(this)

        userModel = preferences.getToken()
        token = userModel.token.toString()

        Log.i("token", token)
    }
}