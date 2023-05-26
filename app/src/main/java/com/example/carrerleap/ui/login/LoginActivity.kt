package com.example.carrerleap.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carrerleap.R
import com.example.carrerleap.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}