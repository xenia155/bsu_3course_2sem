package com.example.task2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentValues
import android.content.Intent
import android.widget.Button

// Constants
private const val TABLE_NAME = "Автомобиль"
private const val COLUMN_ID = "ID"
private const val COLUMN_MARK = "Марка"
private const val COLUMN_NUMBER = "Серийный_номер"
private const val COLUMN_YEAR = "Год_выпуска"

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var buttonShowInfo: Button
    private lateinit var buttonAddRecord: Button
    private lateinit var buttonUpdateRecord: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        createTableIfNotExists()

        buttonShowInfo = findViewById(R.id.buttonShowInfo)
        buttonAddRecord = findViewById(R.id.buttonAddRecord)
        buttonUpdateRecord = findViewById(R.id.buttonUpdateRecord)

        buttonShowInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        buttonAddRecord.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_MARK, "Toyota")
                put(COLUMN_NUMBER, "123456")
                put(COLUMN_YEAR, 2018)
            }
            db.insert(TABLE_NAME, null, values)
            db.close()
        }

        buttonUpdateRecord.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_NUMBER, "789012")
                put(COLUMN_MARK, "BMW")
                put(COLUMN_YEAR, "2020")
            }
            db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf("1"))
            db.close()
        }
    }

    private fun createTableIfNotExists() {
        val db = dbHelper.writableDatabase
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_MARK TEXT," +
                "$COLUMN_NUMBER TEXT," +
                "$COLUMN_YEAR INTEGER" +
                ")")

        db.use {
            val count = it.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null).use { cursor ->
                cursor.moveToFirst()
                cursor.getInt(0)
            }

            if (count == 0) {
                // Удалить все записи, если существуют
                db.delete(TABLE_NAME, null, null)

                // Добавить 5 записей
                val carValues = arrayOf(
                    arrayOf("Mercedes", "345678", 2019),
                    arrayOf("Honda", "567890", 2017),
                    arrayOf("Ford", "901234", 2021),
                    arrayOf("Volkswagen", "890123", 2015),
                    arrayOf("Nissan", "012345", 2022)
                )

                for (car in carValues) {
                    val carData = ContentValues().apply {
                        put(COLUMN_MARK, car[0].toString())
                        put(COLUMN_NUMBER, car[1].toString())
                        put(COLUMN_YEAR, car[2].toString())
                    }
                    db.insert(TABLE_NAME, null, carData)
                }
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
