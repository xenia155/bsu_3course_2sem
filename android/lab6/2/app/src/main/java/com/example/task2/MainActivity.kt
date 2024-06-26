package com.example.task2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var userManager: UserManager
    var age = 0
    var fname = ""
    var lname = ""
    var gender = ""
    private var tv_age: TextView? = null
    private var tv_fname: TextView? = null
    private var tv_lname: TextView? = null
    private var tv_gender: TextView? = null
    private var btn_save: Button? = null
    private var et_fname: EditText? = null
    private var et_lname: EditText? = null
    private var et_age: EditText? = null
    private var switch_gender:  SwitchCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userManager = UserManager(dataStore)
        tv_age = findViewById<View>(R.id.tv_age) as TextView
        tv_fname = findViewById<View>(R.id.tv_fname) as TextView
        tv_lname = findViewById<View>(R.id.tv_lname) as TextView
        tv_gender = findViewById<View>(R.id.tv_gender) as TextView
        btn_save = findViewById<View>(R.id.btn_save) as Button
        et_fname = findViewById<View>(R.id.et_fname) as EditText
        et_lname = findViewById<View>(R.id.et_lname) as EditText
        et_age = findViewById<View>(R.id.et_age) as EditText
        switch_gender = findViewById<View>(R.id.switch_gender) as SwitchCompat
        buttonSave()

        observeData()
    }

    private fun observeData() {

        //Updates age
        userManager.userAgeFlow.asLiveData().observe(this, {
            if (it != null) {
                age = it
                tv_age?.text = it.toString()
            }
        })

        //Updates firstname
        userManager.userFirstNameFlow.asLiveData().observe(this, {
            if (it != null) {
                fname = it
                tv_fname?.text = it
            }
        })

        //Updates lastname
        userManager.userLastNameFlow.asLiveData().observe(this, {
            if (it != null) {
                lname = it
                tv_lname?.text = it
            }
        })

        //Updates gender
        userManager.userGenderFlow.asLiveData().observe(this, {
            if (it != null) {
                gender = if (it) "Male" else "Female"
                tv_gender?.text = gender
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun buttonSave() {

        //Gets the user input and saves it
        btn_save?.setOnClickListener {
            fname = et_fname?.text.toString()
            lname = et_lname?.text.toString()
            age = et_age?.text.toString().toInt()
            val isMale = switch_gender?.isChecked

            //Stores the values
            GlobalScope.launch {
                if (isMale != null) {
                    userManager.storeUser(age, fname, lname, isMale)
                }
            }
        }
    }
}