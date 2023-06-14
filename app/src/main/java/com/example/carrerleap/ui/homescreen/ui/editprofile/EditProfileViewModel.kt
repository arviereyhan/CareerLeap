package com.example.carrerleap.ui.homescreen.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository
import com.example.carrerleap.data.userdata.UpdateProfileRequest
import okhttp3.RequestBody

class EditProfileViewModel(private val repository: DataRepository): ViewModel() {

    fun updateProfile(
        token: String, full_name: RequestBody, date_of_birth: RequestBody, phonenumber: RequestBody, location: RequestBody
    ) = repository.updateProfile(token,full_name,date_of_birth,phonenumber,location)



}