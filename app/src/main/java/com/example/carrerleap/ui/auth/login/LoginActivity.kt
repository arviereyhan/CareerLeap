package com.example.carrerleap.ui.auth.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.databinding.ActivityLoginBinding
import com.example.carrerleap.ui.choose.ChooseActivity
import com.example.carrerleap.ui.homescreen.HomeScreenActivity
import com.example.carrerleap.ui.question.QuestionActivity
import com.example.carrerleap.ui.uploadcv.UploadCvActivity
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        val viewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(
            this@LoginActivity,
            viewModelFactory
        )[LoginViewModel::class.java]
        preferences = Preferences(this)
        userModel = preferences.getToken()


        binding.btnLogin.setOnClickListener {
            processLogin()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun processLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        loginViewModel.postLogin(email, password).observe(this){
            when {
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
                            showLoading(true)
                            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            val data = it.data
                            val loginModel = UserModel(
                                token = data.loginResult.token
                            )
                            Log.d("TOKEN", data.loginResult.token) // mengetes apakah token sudah benar
                            Log.d("TOKEN", loginModel.token!!) // mengetes apakah token sudah benar


                            preferences.saveToken(loginModel)
                            userModel = preferences.getToken()
                            token = userModel.token.toString()
                            loginViewModel.getProfile(token).observe(this){
                                when(it) {
                                    is Result.Success -> {
                                        if (it.data.userProfile?.cvUrl == null){
                                            val intent = Intent(this, UploadCvActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }else if(it.data.userProfile?.cvUrl != null && it.data.userProfile.jobId == null){
                                            val intent = Intent(this, ChooseActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }else if (it.data.userProfile?.cvUrl != null && it.data.userProfile.jobId != null ){
                                            loginViewModel.getScore(token).observe(this){
                                                when (it){
                                                    is Result.Success -> {
                                                        if(it.data.data == null){
                                                            val intent = Intent(this, QuestionActivity::class.java)
                                                            startActivity(intent)
                                                            finish()
                                                        }else if (it.data.data != null){
                                                            val intent = Intent(this, HomeScreenActivity::class.java)
                                                            startActivity(intent)
                                                            finish()
                                                        }
                                                    }
                                                    is Result.Error -> {

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    is Result.Error -> {

                                    }
                                }
                            }
                        }
                        is Result.Error -> {
                            if (it.error == "retrofit2.HttpException: HTTP 401 "){
                                Toast.makeText(this@LoginActivity, "incorrect email or password", Toast.LENGTH_LONG).show()
                            }
                        }

                    }
                }
            }
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
            startActivity(Intent(this, HomeScreenActivity::class.java).also {
                finish()
            })
        }
    }

    companion object{
        const val EXTRA_TOKEN = "extra_token"
    }
}