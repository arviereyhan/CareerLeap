package com.example.carrerleap.ui.homescreen

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.carrerleap.R
import com.example.carrerleap.databinding.ActivityHomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home_screen)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setupWithNavController(navController)

        val text_menu = navView.menu
        for (i in 0 until text_menu.size()) {
            val menuItem = text_menu.getItem(i)
            val spannableString = SpannableString(menuItem.title)
            spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.shades_grey)), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            menuItem.title = spannableString
        }
        //mengatur warna text pada bottom navigation
    }


}