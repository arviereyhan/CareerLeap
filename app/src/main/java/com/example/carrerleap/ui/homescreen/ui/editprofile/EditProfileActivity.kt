package com.example.carrerleap.ui.homescreen.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.carrerleap.R
import com.example.carrerleap.data.userdata.UserData
import com.example.carrerleap.databinding.ActivityEditProfileBinding
import com.example.carrerleap.utils.*
import com.example.carrerleap.utils.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class EditProfileActivity : AppCompatActivity() {
    private lateinit var userData: UserData
    private lateinit var binding: ActivityEditProfileBinding
    private  lateinit var date: String
    private lateinit var editProfileViewModel: EditProfileViewModel
    private  lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private lateinit var token: String
    private lateinit var formatteddate: String
    private var fileImage: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Edit Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = Preferences(this)
        userModel = preferences.getToken() //ambil token untuk update
        token = userModel.token!!


        val viewModelFactory = ViewModelFactory.getInstance(this)
        editProfileViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[EditProfileViewModel::class.java]

        userData = intent.getParcelableExtra("EXTRA_DATA")!!

        if(userData.birthdate!=null){
            formatteddate = formatDate(userData.birthdate!!)
        }

        lifecycleScope.launch{
            fileImage = downloadImage(userData.profileurl!!)!!
            setupView()
        } // hanya pada saat pertama



        binding.editprofileimagebutton.setOnClickListener {
            openFilePicker()
        }

        binding.editnamebutton.setOnClickListener { inputEditText(it,"Name") }
        binding.editbirthdatebutton.setOnClickListener { inputDate(it,"Birthdate") }
        binding.editphonenumberbutton.setOnClickListener { inputEditText(it,"Phone Number") }
        binding.editlocationbutton.setOnClickListener { inputEditText(it,"Location") }

        binding.savebutton.setOnClickListener {
            if (userData.name==null || userData.birthdate==null || userData.phonenumber==null || userData.location==null){
                Toast.makeText(this,"Uncompleted Field! Please Fill All Field!",Toast.LENGTH_LONG).show()
            }
            else{
                UpdateProfile(token,userData.name!!,formatDate(userData.birthdate!!),userData.phonenumber!!,userData.location!!)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Kembali ke HomeFragment
                return true
            }
        }
        return super.onContextItemSelected(item)
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

            val uri = Uri.fromFile(fileImage)
            if (fileImage != null) {
                Log.i("foto","ada")

                binding.profileImage.setImageURI(uri)
            }
            else Log.i("foto","null")





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
                binding.profileImage.setImageURI(uri)

                val myFile = uriToImage(uri, this@EditProfileActivity)
                fileImage = myFile
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

    fun inputEditText(view: View,description: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Enter $description: ")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.edit_text)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->

            if(editText.text.toString()!="") {
                when(description) {
                    "Name" -> userData.name = editText.text.toString()
                    "Phone Number" -> userData.phonenumber = editText.text.toString()
                    "Location" -> userData.location = editText.text.toString()
                }
                setupView()
            }

        }
        builder.show()
    }

    fun inputDate(view: View,description: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Enter $description: ")
        val dialogLayout = inflater.inflate(R.layout.datepicker, null)
        val datePicker = dialogLayout.findViewById<DatePicker>(R.id.date_Picker)
        val today = Calendar.getInstance()
        date = "${today.get(Calendar.DAY_OF_MONTH)}-${today.get(Calendar.MONTH) + 1}-${today.get(Calendar.YEAR)}" //default value untuk tanngal

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            date = "$day-$month-$year"

        }
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            formatteddate = formatDate(date)
            Log.i("date",formatteddate)
            Log.i("date",date)
            userData.birthdate = date
            setupView()
        }
        builder.show()
    }

    fun formatDate(dateString: String): String {
        Log.i("date",dateString)
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun UpdateProfile(token: String, name: String,dateofbirth: String,phonenumber: String,location:String){
        val image = reduceFileImage(fileImage as File)
        val mediaType = "text/plain".toMediaTypeOrNull()
        val fullNamePart = RequestBody.create(mediaType, name)
        val dateOfBirthPart = RequestBody.create(mediaType, dateofbirth)
        val phoneNumberPart = RequestBody.create(mediaType, phonenumber)
        val locationPart = RequestBody.create(mediaType, location)
        val requestImageFile = image.asRequestBody("image/*".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file_profile",
            image.name,
            requestImageFile
        )
        editProfileViewModel.updateProfile(token,fullNamePart,dateOfBirthPart,phoneNumberPart,locationPart,imageMultipart).observe(this){
            when(it){
                is Result.Success -> {
                    Toast.makeText(this, "Update Success!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Error -> {
                    Toast.makeText(this, "Update Failed!", Toast.LENGTH_LONG).show()
                    Log.i("error",it.error)
                }

            }
        }





    }

    suspend fun downloadImage(url: String): File? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection()
            connection.connect()

            val inputStream = connection.getInputStream()
            val cacheDir = applicationContext.cacheDir
            val file = File(cacheDir, "temp_image.jpg")
            val outputStream = FileOutputStream(file)

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.close()
            inputStream.close()

            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun reduceFileImage(file: File): File {
        return file
    }



    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 1
    }
}

