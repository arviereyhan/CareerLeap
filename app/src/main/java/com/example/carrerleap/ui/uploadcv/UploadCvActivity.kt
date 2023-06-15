package com.example.carrerleap.ui.uploadcv

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.R
import com.example.carrerleap.databinding.ActivityUploadCvBinding
import com.example.carrerleap.ui.choose.ChooseActivity
import com.example.carrerleap.utils.CvModel
import com.example.carrerleap.utils.PredictModel
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory
import com.example.carrerleap.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UploadCvActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private var token: String = ""
    private var getFile: File? = null
    private lateinit var binding: ActivityUploadCvBinding
    private lateinit var viewModel: UploadCvViewModel
    private lateinit var cvModel: CvModel
    private var textOutput: String = ""
    private var isCv: String = ""
    private var cvUrl: String = ""
    private var predict: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadCvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = Preferences(this)
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this@UploadCvActivity,
            viewModelFactory
        )[UploadCvViewModel::class.java]

        cvModel = preferences.getFile()
        isCv = cvModel.fileCv.toString()
        Log.i("isCv", isCv)

        userModel = preferences.getToken()
        token = userModel.token.toString()

        setupView()
        uploadHandler()

        Log.i("token", token)

        binding.uploadCvButton.setOnClickListener {
            openFilePicker()
        }

        binding.btnSubmitCv.setOnClickListener {
            uploadCv()
        }

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.loadingState.visibility = View.VISIBLE
        } else {
            binding.loadingState.visibility = View.GONE
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

    private fun uploadCv() {
        if(getFile != null){
            showLoading(true)
            val file = getFile as File
            val requestFile = file.asRequestBody("application/pdf".toMediaType())
            val filePart:MultipartBody.Part = MultipartBody.Part.createFormData("file_cv", file.name, requestFile)
            Log.d("file", filePart.toString())
            viewModel.postCv(filePart, token).observe(this){
                when(it){
                    is Result.Success -> {
                        val data = it.data
                        val uploadModel = CvModel(
                            data.message
                        )
                        preferences.saveFile(uploadModel)
                        Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show()
                        Log.i("UploadCvActivity", "Navigating to ChooseActivity")
                    }
                    is Result.Error -> {
                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.getProfile(token).observe(this){
                    when(it){
                        is Result.Success -> {
                            cvUrl = it.data.userProfile?.cvUrl.toString()
                            viewModel.getPredict(cvUrl).observe(this){
                                when (it) {
                                    is Result.Success -> {
                                        predict = it.data.hasilPrediksi.toString()
                                        val dataPredict = PredictModel(
                                            it.data.hasilPrediksi
                                        )
                                        preferences.savePredict(dataPredict)
                                        textOutput = it.data.textOutput.toString()
                                        Log.d("predict temp", predict)
                                        val intent = Intent(this@UploadCvActivity, ChooseActivity::class.java)
                                        intent.putExtra(ChooseActivity.EXTRA_PREDICT, predict)
                                        startActivity(intent)
                                        finish()
                                    }
                                    is Result.Error -> {
                                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        is Result.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Log.d("cv_url", cvUrl)

            }, 3000)
        } else{
            Toast.makeText(this@UploadCvActivity, "Silakan masukkan berkas CV terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        val chooser = Intent.createChooser(intent, "Choose a CV")
        launcherIntentFile.launch(chooser)
    }

    private val launcherIntentFile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedFile = result.data?.data as Uri
            selectedFile.let { uri ->
                val myFile = uriToFile(uri, this@UploadCvActivity)
                getFile = myFile
            }
        }
        if (getFile != null){
            binding.uploadCvButton.setImageResource(R.drawable.baseline_check_circle_24)
        }
    }
    


    private fun uploadHandler(){
        viewModel.getProfile(token).observe(this){
            when(it){
                is Result.Success -> {
                    if (it.data.userProfile?.cvUrl != null){
                        startActivity(Intent(this, ChooseActivity::class.java).also {
                            finish()
                        })
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}