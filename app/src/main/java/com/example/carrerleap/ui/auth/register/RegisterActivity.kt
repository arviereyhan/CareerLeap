package com.example.carrerleap.ui.auth.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.R
import com.example.carrerleap.databinding.ActivityRegisterBinding
import com.example.carrerleap.ui.auth.login.LoginActivity
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val viewModelFactory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(
            this@RegisterActivity,
            viewModelFactory
        )[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            processRegister()
        }



    }

    private fun processRegister() {
        val name = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        registerViewModel.postRegister(name, email, password).observe(this) {
            when {
                name.isEmpty() -> {
                    binding.usernameEditText.error = "Input Your Name"
                    binding.usernameEditText.requestFocus()
                }

                email.isEmpty() -> {
                    binding.emailEditText.error = "Input Your Email"
                    binding.emailEditText.requestFocus()
                }

                password.isEmpty() -> {
                    binding.passwordEditText.error = "Input your Password"
                    binding.passwordEditText.requestFocus()
                }
                else -> {
                    when(it){
                        is Result.Success -> {
                            Toast.makeText(this@RegisterActivity, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is Result.Error -> {
                            Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
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
}