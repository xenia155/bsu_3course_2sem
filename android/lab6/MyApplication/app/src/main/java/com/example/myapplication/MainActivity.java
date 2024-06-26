package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TextView textView;
    private Button btnShowRecords;
    private Button btnShowTotalHours;

    private EditText editTextMinDisciplines;
    private Button btnShowTeachers;

    private Button btnShowTeachersByDepartment;

    private Button btnSortTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        textView = findViewById(R.id.textView);
        btnShowRecords = findViewById(R.id.btnShowRecords);
        btnShowTotalHours = findViewById(R.id.btnShowTotalHours);
        btnShowTeachers = findViewById(R.id.btnShowTeachers);
        btnShowTeachersByDepartment = findViewById(R.id.btnShowTeachersByDepartment);
        editTextMinDisciplines = findViewById(R.id.editTextMinDisciplines);
        btnSortTeachers = findViewById(R.id.btnSortTeachers);

        btnShowRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllRecords();
            }
        });

        btnShowTotalHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTotalHours();
            }
        });


        btnShowTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = editTextMinDisciplines.getText().toString().trim();

                if (!TextUtils.isEmpty(inputValue)) {

                    int filterValue = Integer.parseInt(inputValue);
                    showTeachers(filterValue);
                } else {
                    Toast.makeText(MainActivity.this, "Пожалуйста, введите число", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowTeachersByDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeachersByDepartment();
            }
        });

        btnSortTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeachersByTotalDisciplines();
            }
        });
    }

    private void showAllRecords() {
        Cursor cursor = databaseHelper.getAllDepartments();
        StringBuilder stringBuilder = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String head = cursor.getString(cursor.getColumnIndex("head"));

                stringBuilder.append("ID: ").append(id).append("\n");
                stringBuilder.append("Name: ").append(name).append("\n");
                stringBuilder.append("Head: ").append(head).append("\n");
                stringBuilder.append("\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        textView.setText(stringBuilder.toString());
    }

    private void showTotalHours() {
        Cursor cursor = databaseHelper.getTotalHoursByTeacher();
        StringBuilder stringBuilder = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int teacherId = cursor.getInt(cursor.getColumnIndex("teacher_id"));
                @SuppressLint("Range") int lectureHours = cursor.getInt(cursor.getColumnIndex("lecture_hours"));
                @SuppressLint("Range") int labHours = cursor.getInt(cursor.getColumnIndex("lab_hours"));

                stringBuilder.append("Teacher ID: ").append(teacherId).append("\n");
                stringBuilder.append("SUM Lecture Hours: ").append(lectureHours).append("\n");
                stringBuilder.append("SUM Lab Hours: ").append(labHours).append("\n");
                stringBuilder.append("\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        textView.setText(stringBuilder.toString());
    }

    private void showTeachers(int filterValue) {
        Cursor cursor = databaseHelper.getTeachersWithMinDisciplines(filterValue);
        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder builder = new StringBuilder();
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int disciplineCount = cursor.getInt(cursor.getColumnIndex("discipline_count"));

                builder.append("ID: ").append(id).append(", ");
                builder.append("Name: ").append(name).append(", ");
                builder.append("Total Disciplines: ").append(disciplineCount).append("\n");
                builder.append("\n");
            } while (cursor.moveToNext());

            cursor.close();

            textView.setText(builder.toString());
        } else {
            textView.setText("No records found.");
        }
    }
    private void showTeachersByDepartment() {
        Cursor cursor = databaseHelper.getTeachersByDepartment();
        StringBuilder stringBuilder = new StringBuilder();

        String currentDepartment = null;

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String departmentName = cursor.getString(cursor.getColumnIndex("department_name"));
                @SuppressLint("Range") String teacherName = cursor.getString(cursor.getColumnIndex("teacher_name"));

                if (!departmentName.equals(currentDepartment)) {
                    currentDepartment = departmentName;
                    stringBuilder.append("Department: ").append(departmentName).append("\n");
                }

                stringBuilder.append("Teacher: ").append(teacherName).append("\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        textView.setText(stringBuilder.toString());
    }

    private void showTeachersByTotalDisciplines() {
        Cursor cursor = databaseHelper.getTeachersByTotalDisciplines();
        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder builder = new StringBuilder();
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int totalDisciplines = cursor.getInt(cursor.getColumnIndex("total_disciplines"));

                builder.append("ID: ").append(id).append(", ");
                builder.append("Name: ").append(name).append(", ");
                builder.append("Total Disciplines: ").append(totalDisciplines).append("\n");
                builder.append("\n");
            } while (cursor.moveToNext());

            cursor.close();

            textView.setText(builder.toString());
        } else {
            textView.setText("No records found.");
        }
    }
}