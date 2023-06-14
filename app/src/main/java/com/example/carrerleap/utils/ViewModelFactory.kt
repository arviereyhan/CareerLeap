package com.example.carrerleap.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.data.repository.DataRepository
import com.example.carrerleap.ui.auth.login.LoginViewModel
import com.example.carrerleap.ui.auth.register.RegisterViewModel
import com.example.carrerleap.ui.homescreen.ui.editprofile.EditProfileViewModel
import com.example.carrerleap.ui.homescreen.ui.profile.ProfileViewModel

class ViewModelFactory(private val repository: DataRepository): ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(repository) as T
            }  else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repository) as T
            }
            else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(repository) as T
            }
            else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
                return EditProfileViewModel(repository) as T
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
