package com.example.carrerleap.ui.choose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.carrerleap.R
import com.example.carrerleap.data.dummy.JobsDataSource
import com.example.carrerleap.databinding.ActivityChooseBinding
import com.example.carrerleap.ui.question.QuestionActivity

class ChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseBinding
    private var selectedItem: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jobs = JobsDataSource.question
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, jobs)

        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autocompleteTV.setAdapter(arrayAdapter)

        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id->
                selectedItem = parent.getItemAtPosition(position) as String
            }

        binding.btnToQuestion.setOnClickListener {
            if (selectedItem != null) {
                // Lakukan tindakan berdasarkan nilai yang dipilih
                Toast.makeText(this, "Anda memilih: $selectedItem", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ChooseActivity, QuestionActivity::class.java)
                intent.putExtra(QuestionActivity.JOBS, selectedItem)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Silakan pilih item dari dropdown", Toast.LENGTH_SHORT).show()
            }
        }

    }




}