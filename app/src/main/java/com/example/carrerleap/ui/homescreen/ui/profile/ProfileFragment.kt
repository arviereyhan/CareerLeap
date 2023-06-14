package com.example.carrerleap.ui.homescreen.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.carrerleap.R
import com.example.carrerleap.data.userdata.UserData
import com.example.carrerleap.databinding.FragmentProfileBinding
import com.example.carrerleap.ui.auth.login.LoginViewModel
import com.example.carrerleap.ui.homescreen.ui.editprofile.EditProfileActivity
import com.example.carrerleap.utils.Preferences
import com.example.carrerleap.utils.Result
import com.example.carrerleap.utils.UserModel
import com.example.carrerleap.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private lateinit var preferences: Preferences
    private lateinit var userModel: UserModel
    private  lateinit var token: String
    private lateinit var profileViewModel: ProfileViewModel
    private  lateinit var userData: UserData
    private var isLoaded: Boolean = false
    private  var outputDateString: String? = null





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        preferences = Preferences(requireContext())

        userModel = preferences.getToken() //ambil token untuk get_profile
        token = userModel.token!!

        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        profileViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[ProfileViewModel::class.java]

        setupview()

        binding.editButton1.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            if(isLoaded){               //mengecek apakah data sudah diload dari server, karena jika pindah activity masih belum keload nanti kena NPE
                intent.putExtra("EXTRA_DATA",userData)
                startActivity(intent)
            }

        }

        binding.Logoutbutton.setOnClickListener { withEditText(it) }

        return root
    }


    override fun onResume() {
        super.onResume()              // Agar data tetap up-to-date ketika kembali dari edit profile activity
        setupview()
     }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupview(){
        profileViewModel.get_profile(token).observe(viewLifecycleOwner){
            when(it) {
                is Result.Success -> {

                    if(it.data.userProfile?.fullName == null){
                        binding.textViewName.text = getString(R.string.not_available)
                    }
                    else {
                        binding.textViewName.text = it.data.userProfile?.fullName
                    }

                    if(it.data.userProfile?.dateOfBirth == null){
                        binding.textViewBirth.text = "Date of birth is empty"
                    }
                    else {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd-MM-YYYY", Locale.getDefault())
                        val inputDate: Date = inputFormat.parse(it.data.userProfile?.dateOfBirth)
                        outputDateString = outputFormat.format(inputDate)
                        binding.textViewBirth.text = outputDateString
                    }

                    if(it.data.userProfile?.email == null){
                        binding.textViewEmail.text = "email is empty"
                        Log.i("userdata","1")

                    }
                    else{
                        binding.textViewEmail.text = it.data.userProfile?.email
                    }

                    if(it.data.userProfile?.location == null){
                        binding.textViewLocation.text = "Location is empty"
                    }
                    else {
                        binding.textViewLocation.text = it.data.userProfile?.location
                    }

                    if(it.data.userProfile?.phoneNumber== null){
                        binding.textViewPhonenumber.text = "Phone number is empty"
                    }
                    else {
                        binding.textViewPhonenumber.text = it.data.userProfile?.phoneNumber
                    }

                    binding.textViewCareer.text = "Career is empty"


                    userData = UserData(it.data.userProfile?.fullName,
                        "dummy profile url",
                        outputDateString,
                        it.data.userProfile?.phoneNumber,
                        it.data.userProfile?.email,
                        it.data.userProfile?.location
                    )
                    isLoaded = true
                    Log.i("isloaded",isLoaded.toString())


                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun withEditText(view: View) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        builder.setTitle("Masukkan Nama: ")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.edit_text)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i -> Toast.makeText(context, "EditText is " + editText.text.toString(), Toast.LENGTH_SHORT).show() }
        builder.show()
    }
}