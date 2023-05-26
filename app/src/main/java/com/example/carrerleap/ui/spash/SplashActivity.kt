package com.example.carrerleap.ui.spash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.carrerleap.databinding.ActivitySplashBinding
import com.example.carrerleap.ui.auth.LoginRegisterActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

        val logo = ObjectAnimator.ofFloat(binding.splashLogo, View.ALPHA, 1f).setDuration(1000)
        AnimatorSet().apply {
            play(logo)
            start()
        }
    }
}