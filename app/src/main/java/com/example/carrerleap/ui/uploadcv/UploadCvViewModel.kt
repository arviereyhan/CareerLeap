package com.example.carrerleap.ui.uploadcv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository
import okhttp3.MultipartBody
import java.io.File

class UploadCvViewModel(private val repository: DataRepository): ViewModel() {
    private val selectedFile: MutableLiveData<File> = MutableLiveData()

    fun setSelectedFile(file: File) {
        selectedFile.value = file
    }

    fun getSelectedFile(): File? {
        return selectedFile.value
    }

    fun postCv(file: MultipartBody.Part, token: String) = repository.uploadCv(file, token)
}