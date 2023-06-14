package com.example.carrerleap.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.data.repository.DataRepository
import com.example.carrerleap.ui.auth.login.LoginViewModel
import com.example.carrerleap.ui.auth.register.RegisterViewModel
import com.example.carrerleap.ui.choose.ChooseViewModel
import com.example.carrerleap.ui.course.CourseViewModel
import com.example.carrerleap.ui.homescreen.ui.home.HomeViewModel
import com.example.carrerleap.ui.uploadcv.UploadCvViewModel
import com.example.carrerleap.ui.homescreen.ui.profile.ProfileViewModel
import com.example.carrerleap.ui.question.QuestionViewModel
import com.example.carrerleap.ui.spash.SplashViewModel

class ViewModelFactory(private val repository: DataRepository): ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(repository) as T
            }  else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repository) as T
            } else if (modelClass.isAssignableFrom(UploadCvViewModel::class.java)) {
                return UploadCvViewModel(repository) as T
            } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(repository) as T
            } else if (modelClass.isAssignableFrom(ChooseViewModel::class.java)) {
                return ChooseViewModel(repository) as T
            } else if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
                return QuestionViewModel(repository) as T
            }else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository) as T
            }else if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
                return CourseViewModel(repository) as T
            }else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
                return SplashViewModel(repository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }

        companion object{
            @Volatile
            private var instance: ViewModelFactory? = null
            fun getInstance(context: Context): ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory(Injection.provideRepository(context))
                }.also {
                    instance = it
                }
        }
    }
