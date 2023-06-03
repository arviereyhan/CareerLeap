package com.example.carrerleap.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.carrerleap.databinding.ActivityLoginRegisterBinding
import com.example.carrerleap.ui.auth.login.LoginActivity
import com.example.carrerleap.ui.auth.register.RegisterActivity
import com.example.carrerleap.ui.uploadcv.UploadCvActivity
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.UserModel

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var userModel: UserModel
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)

        userModel = preferences.getToken()

        setupView()
        loginHandler()

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LoginRegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginRegisterActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun loginHandler(){
        if (userModel.token != null) {
            startActivity(Intent(this, UploadCvActivity::class.java).also {
                finish()
            })

        }
    }

}