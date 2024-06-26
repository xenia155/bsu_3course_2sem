package com.example.task4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper() {

        companion object {
        const val DATABASE_NAME = "Test"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Автомобиль"
        const val COLUMN_ID = "ID"
        const val COLUMN_BRAND = "Марка"
        const val COLUMN_SERIAL_NUMBER = "Серийный номер"
        const val COLUMN_YEAR = "Год выпуска"
        }

        override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME (" +
        "$COLUMN_ID INTEGER PRIMARY KEY," +
        "$COLUMN_BRAND TEXT," +
        "$COLUMN_SERIAL_NUMBER TEXT," +
        "$COLUMN_YEAR INTEGER" +
        ");"
        db?.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(query)
        onCreate(db)
        }

        // Другие методы, такие как insertData(), getData(), updateData() и deleteData(), могут быть добавлены в соответствии с вашими потребностями.

        }
