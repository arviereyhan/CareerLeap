package com.example.carrerleap.ui.homescreen.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.example.carrerleap.R
import com.example.carrerleap.data.userdata.UserData
import com.example.carrerleap.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var userData: UserData
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getParcelableExtra("EXTRA_DATA")!!

        setupView()

        binding.editprofileimagebutton.setOnClickListener {
            openFilePicker()
        }





    }

    private fun setupView() {
        if (userData.name==null){
            binding.textViewName.text = getString(R.string.not_available)
        }
        else{
            binding.textViewName.text = userData.name
        }


        if (userData.birthdate==null){
            binding.birthdate.text = getString(R.string.not_available)
        }
        else{
            binding.birthdate.text = userData.birthdate
        }

        if (userData.email==null){
            binding.email.text = getString(R.string.not_available)
        }
        else{
            binding.email.text = userData.email
        }

        if (userData.location==null){
            binding.location.text = getString(R.string.not_available)
        }
        else{
            binding.location.text = userData.location
        }
        if (userData.phonenumber==null){
            binding.phonenumber.text = getString(R.string.not_available)
        }
        else{
            binding.phonenumber.text = userData.phonenumber
        }


    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val fileName = getFileName(uri)
                Toast.makeText(this, "Selected file: $fileName", Toast.LENGTH_SHORT).show()
                // Handle the selected file URI here
                // You can pass the URI to the uploadFile() function or perform any other desired actions
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    fileName = cursor.getString(displayNameIndex)
                } else {
                    // Fallback option to extract the file name from the URI
                    fileName = uri.lastPathSegment
                }
            }
        }
        return fileName
    }

    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 1
    }
}