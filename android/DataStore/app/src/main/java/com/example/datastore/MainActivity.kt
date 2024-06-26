package com.example.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var userManager: UserManager
    var age = 0
    var fname = ""
    var lname = ""
    var gender = ""

    private lateinit var tvAge: TextView
    private lateinit var tvFname: TextView
    private lateinit var tvLname: TextView
    private lateinit var tvGender: TextView
    private lateinit var etFname: EditText
    private lateinit var etLname: EditText
    private lateinit var etAge: EditText
    private lateinit var switchGender: Switch
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get reference to our userManager class
        userManager = UserManager(dataStore)

        // Initialize views using findViewById
        tvAge = findViewById(R.id.tv_age)
        tvFname = findViewById(R.id.tv_fname)
        tvLname = findViewById(R.id.tv_lname)
        tvGender = findViewById(R.id.tv_gender)
        etFname = findViewById(R.id.et_fname)
        etLname = findViewById(R.id.et_lname)
        etAge = findViewById(R.id.et_age)
        switchGender = findViewById(R.id.switch_gender)
        btnSave = findViewById(R.id.btn_save)

        buttonSave()

        observeData()
    }

    private fun observeData() {

        //Updates age
        userManager.userAgeFlow.asLiveData().observe(this, { it ->
            if (it != null) {
                age = it
                tvAge.text = it.toString()
            }
        })

        //Updates firstname
        userManager.userFirstNameFlow.asLiveData().observe(this, { it ->
            if (it != null) {
                fname = it
                tvFname.text = it
            }
        })

        //Updates lastname
        userManager.userLastNameFlow.asLiveData().observe(this, { it ->
            if (it != null) {
                lname = it
                tvLname.text = it
            }
        })

        //Updates gender
        userManager.userGenderFlow.asLiveData().observe(this, { it ->
            if (it != null) {
                gender = if (it) "Male" else "Female"
                tvGender.text = gender
            }
        })
    }

    private fun buttonSave() {

        //Gets the user input and saves it
        btnSave.setOnClickListener {
            fname = etFname.text.toString()
            lname = etLname.text.toString()
            age = etAge.text.toString().toInt()
            val isMale = switchGender.isChecked

            //Stores the values
            GlobalScope.launch {
                userManager.storeUser(age, fname, lname, isMale)
            }
        }
    }
}