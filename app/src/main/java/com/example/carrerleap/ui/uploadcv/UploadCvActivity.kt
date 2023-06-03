package com.example.carrerleap.ui.uploadcv

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.example.carrerleap.R
import com.example.carrerleap.databinding.ActivityUploadCvBinding
import com.example.carrerleap.ui.choose.ChooseActivity
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.UserModel


class UploadCvActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private var token: String = ""
    private lateinit var binding: ActivityUploadCvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadCvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = Preferences(this)

        userModel = preferences.getToken()
        token = userModel.token.toString()

        Log.i("token", token)

        binding.uploadCvButton.setOnClickListener {
            openFilePicker()
        }

        binding.btnSubmitCv.setOnClickListener {
            val intent = Intent(this@UploadCvActivity, ChooseActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
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