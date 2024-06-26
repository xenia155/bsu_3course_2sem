package com.example.task2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val TABLE_NAME = "Автомобиль"
private const val COLUMN_ID = "ID"
private const val COLUMN_MARK = "Марка"
private const val COLUMN_NUMBER = "Серийный_номер"
private const val COLUMN_YEAR = "Год_выпуска"
private const val COLUMN_COLOR = "Цвет"
private const val COLUMN_YEAR_TECH = "Год_техосмотра"
private const val COLUMN_PRICE = "Цена"

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Cars.db"
        private const val DATABASE_VERSION = 2
        private const val TEMP_TABLE_NAME = "Temp_Автомобиль"
        private const val TEMP_TABLE_NAME2 = "Temp_Автомобиль2"
    }

    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS Автомобиль (" +
//                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "Марка TEXT," +
//                "Серийный_номер TEXT," +
//                "Год_выпуска INTEGER" +
//                ")")

            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_MARK TEXT," +
            "$COLUMN_NUMBER TEXT," +
            "$COLUMN_YEAR INTEGER," +
            "$COLUMN_COLOR TEXT," +
            "$COLUMN_YEAR_TECH INTEGER," +
            "$COLUMN_PRICE TEXT" +
            ")")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS $TEMP_TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_MARK TEXT," +
                "$COLUMN_NUMBER TEXT," +
                "$COLUMN_YEAR INTEGER," +
                "$COLUMN_COLOR TEXT," +
                "$COLUMN_YEAR_TECH INTEGER," +
                "$COLUMN_PRICE TEXT" +
                ")")

            db.execSQL(
                "CREATE TABLE IF NOT EXISTS $TEMP_TABLE_NAME2 (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$COLUMN_COLOR TEXT," +
                        "$COLUMN_YEAR_TECH INTEGER," +
                        "$COLUMN_PRICE TEXT" +
                        ")"
            )

            db.execSQL("INSERT INTO $TEMP_TABLE_NAME2 ($COLUMN_COLOR, $COLUMN_YEAR_TECH, $COLUMN_PRICE) VALUES " +
                    "('Красный', 2021, '50000$'), " +
                    "('Синий', 2019, '10000\$'), " +
                    "('Чёрный', 2022, '30000\$'), " +
                    "('Серебряный', 2018, '45000\$'), " +
                    "('Белый', 2023, '100000\$')")

            // Копируем данные из старых таблиц во временную таблицу
            db.execSQL(
                "INSERT INTO $TEMP_TABLE_NAME " +
                        "($COLUMN_ID, $COLUMN_MARK, $COLUMN_NUMBER, $COLUMN_YEAR, $COLUMN_COLOR, $COLUMN_YEAR_TECH, $COLUMN_PRICE) " +
                        "SELECT t1.$COLUMN_ID, t1.$COLUMN_MARK, t1.$COLUMN_NUMBER, t1.$COLUMN_YEAR, t2.$COLUMN_COLOR, t2.$COLUMN_YEAR_TECH, t2.$COLUMN_PRICE " +
                        "FROM $TABLE_NAME t1 " +
                        "JOIN $TEMP_TABLE_NAME2 t2 ON t1.$COLUMN_ID = t2.$COLUMN_ID"
            )

            // Удаляем старую таблицу
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $TEMP_TABLE_NAME2")

            // Создаем новую таблицу
            onCreate(db)

            // Копируем данные из временной таблицы в новую таблицу
            db.execSQL("INSERT INTO $TABLE_NAME SELECT * FROM $TEMP_TABLE_NAME")

            // Удаляем временную таблицу
            db.execSQL("DROP TABLE IF EXISTS $TEMP_TABLE_NAME")
        }
    }
}