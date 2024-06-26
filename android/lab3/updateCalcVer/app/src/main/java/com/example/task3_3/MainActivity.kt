package com.example.task3_3

import Calc
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.example.task3_3.R



class MainActivity : AppCompatActivity() {

    var options = arrayOf("Calculator", "History")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_horizontal)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            options
        )
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                when (position) {
                    0 -> startActivity(Intent(this@MainActivity, Calc::class.java))
                    1 -> startActivity(Intent(this@MainActivity, History::class.java))

                }
                Toast.makeText(
                    applicationContext, "Вы выбрали " +
                            parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT
                ).show()
            }
    }
}

