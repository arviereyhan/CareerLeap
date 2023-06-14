package com.example.carrerleap.ui.homescreen.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.R
import com.example.carrerleap.data.userdata.UpdateProfileRequest
import com.example.carrerleap.data.userdata.UserData
import com.example.carrerleap.databinding.ActivityEditProfileBinding
import com.example.carrerleap.ui.auth.login.LoginActivity
import com.example.carrerleap.ui.homescreen.HomeScreenActivity
import com.example.carrerleap.ui.homescreen.ui.profile.ProfileViewModel
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var userData: UserData
    private lateinit var binding: ActivityEditProfileBinding
    private  lateinit var date: String
    private  lateinit var updatedata: UpdateProfileRequest
    private lateinit var editProfileViewModel: EditProfileViewModel
    private  lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private lateinit var token: String
    private lateinit var formatteddate: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)
        userModel = preferences.getToken() //ambil token untuk update
        token = userModel.token!!

        val viewModelFactory = ViewModelFactory.getInstance(this)
        editProfileViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[EditProfileViewModel::class.java]

        userData = intent.getParcelableExtra("EXTRA_DATA")!!
        formatteddate = formatDate(userData.birthdate!!)

        setupView()

        binding.editprofileimagebutton.setOnClickListener {
            openFilePicker()
        }

        binding.editnamebutton.setOnClickListener { inputEditText(it,"Name") }
        binding.editbirthdatebutton.setOnClickListener { inputDate(it,"Birthdate") }
        binding.editphonenumberbutton.setOnClickListener { inputEditText(it,"Phone Number") }
        binding.editemailbutton.setOnClickListener { inputEditText(it,"Email") }
        binding.editlocationbutton.setOnClickListener { inputEditText(it,"Location") }

        binding.savebutton.setOnClickListener {
            updatedata = UpdateProfileRequest(userData.name!!,formatDate(userData.birthdate!!),userData.phonenumber!!,userData.location!!)
            Log.i("update","${updatedata.full_name} - $formatteddate - ${updatedata.location} - ${updatedata.phone_number}")
            UpdateProfile(token,updatedata)
        }

        binding.profileImage.setImageDrawable()

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

    fun UpdateProfile(token: String, data: UpdateProfileRequest){
        editProfileViewModel.updateProfile(token,data).observe(this){
            when(it){
                is Result.Success -> {
                    Toast.makeText(this, "Update Success!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Error -> {
                    Toast.makeText(this, "Update Failed! Try Again!", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 1
    }
}

