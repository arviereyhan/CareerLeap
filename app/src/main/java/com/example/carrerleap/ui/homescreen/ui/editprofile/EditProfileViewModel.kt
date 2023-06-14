package com.example.carrerleap.ui.homescreen.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository
import com.example.carrerleap.data.userdata.UpdateProfileRequest

class EditProfileViewModel(private val repository: DataRepository): ViewModel() {

    fun updateProfile(token: String,data: UpdateProfileRequest) = repository.updateProfile(token,data)



}