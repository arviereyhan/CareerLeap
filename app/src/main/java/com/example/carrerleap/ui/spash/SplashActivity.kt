package com.example.carrerleap.ui.spash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.databinding.ActivitySplashBinding
import com.example.carrerleap.ui.auth.LoginRegisterActivity
import com.example.carrerleap.ui.choose.ChooseActivity
import com.example.carrerleap.ui.homescreen.HomeScreenActivity
import com.example.carrerleap.ui.homescreen.ui.home.HomeFragment
import com.example.carrerleap.ui.question.QuestionActivity
import com.example.carrerleap.ui.uploadcv.UploadCvViewModel
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this@SplashActivity,
            viewModelFactory
        )[SplashViewModel::class.java]

        preferences = Preferences(this)
        userModel = preferences.getToken()
        token = userModel.token.toString()

        Handler(Looper.getMainLooper()).postDelayed({
           splashHandler()
        }, 3000)

        val logo = ObjectAnimator.ofFloat(binding.splashLogo, View.ALPHA, 1f).setDuration(1000)
        AnimatorSet().apply {
            play(logo)
            start()
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

    private fun splashHandler(){
        if (userModel.token != null){
            viewModel.getProfile(token).observe(this){
                when (it){
                    is Result.Success -> {
                        val data = it.data.userProfile
                        if(data?.cvUrl != null && data.jobId != null ){
                            viewModel.getScore(token).observe(this){
                                when (it){
                                    is Result.Success -> {
                                        if (it.data.data != null){
                                            val intent = Intent(this@SplashActivity, HomeScreenActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else if (it.data.data == null){
                                            val intent = Intent(this@SplashActivity, QuestionActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                                    is Result.Error -> {

                                    }
                                }
                            }
                        } else if (data?.cvUrl == null){
                            val intent = Intent(this@SplashActivity, HomeFragment::class.java)
                            startActivity(intent)
                            finish()
                        } else if (data.jobId == null){
                            val intent = Intent(this@SplashActivity, ChooseActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    is Result.Error -> {

                    }
                }
            }
        } else {
            val intent = Intent(this@SplashActivity, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}