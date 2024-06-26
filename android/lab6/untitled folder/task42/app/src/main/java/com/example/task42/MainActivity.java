package com.example.task42;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayData();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void displayData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "ID",
                "Brand",
                "SerialNumber",
                "Year"
        };

        Cursor cursor = db.query(
                "Car",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder data = new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String brand = cursor.getString(cursor.getColumnIndexOrThrow("Brand"));
            String serialNumber = cursor.getString(cursor.getColumnIndexOrThrow("SerialNumber"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("Year"));

            data.append("ID: ").append(id)
                    .append(", Brand: ").append(brand)
                    .append(", Serial Number: ").append(serialNumber)
                    .append(", Year: ").append(year)
                    .append("\n");
        }

        cursor.close();
        db.close();

        Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
    }

    private void insertData() {


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Brand", "Toyota");
        values.put("SerialNumber", "123456789");
        values.put("Year", 2022);

        long newRowId = db.insert("Car", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при добавлении записи", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void updateData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Brand", "Honda");

        String selection = "ID = ?";
        String[] selectionArgs = { "1" };

        int count = db.update(
                "Car",
                values,
                selection,
                selectionArgs
        );

        if (count > 0) {
            Toast.makeText(this, "Запись изменена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при изменении записи", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}