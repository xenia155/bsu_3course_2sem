package com.example.task3_3

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task3_3.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var etInput1: EditText
    private lateinit var etInput2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_horizontal)

        spinner1 = findViewById(R.id.spinner1)

        val generationMethods = resources.getStringArray(R.array.generation_methods)
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, generationMethods)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter1

        spinner2 = findViewById(R.id.spinner2)
        etInput1 = findViewById(R.id.editTextGenerated1)
        etInput2 = findViewById(R.id.editTextGenerated2)

        val operationAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.operation_array,
            android.R.layout.simple_spinner_item
        )
        operationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = operationAdapter
    }


    @SuppressLint("StringFormatMatches")
    fun generateRandomNumber(view: View) {
        val min = findViewById<EditText>(R.id.editTextMin).text.toString().toDoubleOrNull() ?: return
        val max = findViewById<EditText>(R.id.editTextMax).text.toString().toDoubleOrNull() ?: return
        val random = Random()

        val randomNumber1 = when (spinner1.selectedItemPosition) {
            0 -> random.nextInt((max - min).toInt() + 1) + min.toInt()
            1 -> (random.nextDouble() * (max - min)) + min
            2 -> {
                var randomEven = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + min.toInt()
                if (randomEven > max) randomEven -= 2
                randomEven
            }
            3 -> {
                var randomOdd = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + 1 + min.toInt()
                if (randomOdd > max) randomOdd -= 2
                randomOdd
            }
            else -> return
        }

        findViewById<EditText>(R.id.editTextGenerated1).setText(randomNumber1.toString())

        val randomNumber2 = when (spinner1.selectedItemPosition) {
            0 -> random.nextInt((max - min).toInt() + 1) + min.toInt()
            1 -> (random.nextDouble() * (max - min)) + min
            2 -> {
                var randomEven = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + min.toInt()
                if (randomEven > max) randomEven -= 2
                randomEven
            }
            3 -> {
                var randomOdd = random.nextInt(((max - min) / 2).toInt() + 1) * 2 + 1 + min.toInt()
                // Если случайно сгенерированное нечетное число больше максимального, уменьшаем его на 2
                if (randomOdd > max) randomOdd -= 2
                randomOdd
            }
            else -> return
        }
        findViewById<EditText>(R.id.editTextGenerated2).setText(randomNumber2.toString())
    }





    fun performOperation(view: View) {
        val num1 = etInput1.text.toString().toDoubleOrNull() ?: return
        val num2 = etInput2.text.toString().toDoubleOrNull() ?: return

        val operation = when (spinner2.selectedItemPosition) {
            0 -> num1 + num2
            1 -> num1 - num2
            2 -> num1 * num2
            3 -> {
                if (num2 != 0.0) num1 / num2 else {
                    Toast.makeText(this, getString(R.string.divide_by_zero), Toast.LENGTH_SHORT).show()
                    return
                }
            }
            else -> return
        }

        Toast.makeText(this, getString(R.string.result, operation), Toast.LENGTH_SHORT).show()
    }
}
