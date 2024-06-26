package com.example.task2

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView

// Constants
private const val TABLE_NAME = "Автомобиль"
private const val COLUMN_MARK = "Марка"
private const val COLUMN_NUMBER = "Серийный_номер"
private const val COLUMN_YEAR = "Год_выпуска"
private const val COLUMN_COLOR = "Цвет"
private const val COLUMN_YEAR_TECH = "Год_техосмотра"
private const val COLUMN_PRICE = "Цена"

class InfoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        dbHelper = DatabaseHelper(this)

        val carList = getCarListFromDatabase()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, carList)
        val carViewBooks = findViewById<ListView>(R.id.listViewCars)
        carViewBooks.adapter = adapter
    }

    private fun getCarListFromDatabase(): List<String> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val carList = mutableListOf<String>()

        cursor.use {
            while (cursor.moveToNext()) {
                val mark = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MARK))
                val number = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMBER))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR))
                val color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR))
                val year_tech = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YEAR_TECH))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
                carList.add("$mark - $number ($year) - $color - $year_tech - $price")
//                carList.add("$mark - $number ($year)")
            }
        }

        return carList
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}