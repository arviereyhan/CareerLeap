package com.example.carrerleap.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carrerleap.databinding.ActivityLoginRegisterBinding
import com.example.carrerleap.ui.auth.login.LoginActivity
import com.example.carrerleap.ui.auth.register.RegisterActivity

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LoginRegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginRegisterActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

}