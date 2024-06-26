package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "departments.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS departments (id INTEGER PRIMARY KEY, name TEXT, head TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS teachers (id INTEGER PRIMARY KEY, name TEXT, department_id INTEGER, FOREIGN KEY (department_id) REFERENCES departments (id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS disciplines (id INTEGER PRIMARY KEY, name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS teaching_hours (id INTEGER PRIMARY KEY, teacher_id INTEGER, discipline_id INTEGER, lecture_hours INTEGER, lab_hours INTEGER, FOREIGN KEY (teacher_id) REFERENCES teachers (id), FOREIGN KEY (discipline_id) REFERENCES disciplines (id))");

        // Проверяем наличие записей в таблицах
        if (checkIfTableIsEmpty(db, "departments") &&
                checkIfTableIsEmpty(db, "teachers") &&
                checkIfTableIsEmpty(db, "disciplines") &&
                checkIfTableIsEmpty(db, "teaching_hours")) {
            // Выполняем вставку записей только если таблицы пусты
            db.execSQL("INSERT INTO departments (id, name, head) VALUES (1, 'Department of Computer Science', 'John Smith')");
            db.execSQL("INSERT INTO departments (id, name, head) VALUES (2, 'Department of Mathematics', 'Emily Johnson')");
            db.execSQL("INSERT INTO departments (id, name, head) VALUES (3, 'Department of Physics', 'Michael Davis')");

            db.execSQL("INSERT INTO teachers (id, name, department_id) VALUES (1, 'Alice Brown', 1)");
            db.execSQL("INSERT INTO teachers (id, name, department_id) VALUES (2, 'Bob Wilson', 1)");
            db.execSQL("INSERT INTO teachers (id, name, department_id) VALUES (3, 'Carol Davis', 2)");
            db.execSQL("INSERT INTO teachers (id, name, department_id) VALUES (4, 'David Lee', 2)");
            db.execSQL("INSERT INTO teachers (id, name, department_id) VALUES (5, 'Emma Johnson', 3)");

            db.execSQL("INSERT INTO disciplines (id, name) VALUES (1, 'Introduction to Programming')");
            db.execSQL("INSERT INTO disciplines (id, name) VALUES (2, 'Data Structures')");
            db.execSQL("INSERT INTO disciplines (id, name) VALUES (3, 'Algorithms')");
            db.execSQL("INSERT INTO disciplines (id, name) VALUES (4, 'Calculus')");
            db.execSQL("INSERT INTO disciplines (id, name) VALUES (5, 'Linear Algebra')");
            db.execSQL("INSERT INTO disciplines (id, name) VALUES (6, 'Mechanics')");

            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (1, 1, 1, 3, 2)");
            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (2, 1, 2, 2, 1)");
            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (3, 2, 1, 2, 1)");
            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (4, 3, 2, 1, 2)");
            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (5, 4, 3, 3, 2)");
            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (6, 5, 4, 4, 1)");
            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (7, 5, 5, 3, 1)");
            db.execSQL("INSERT INTO teaching_hours (id, teacher_id, discipline_id, lecture_hours, lab_hours) VALUES (8, 5, 6, 2, 2)");
        }
    }

    private boolean checkIfTableIsEmpty(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Ничего не делаем, так как не предусмотрено обновление базы данных
    }

    public Cursor getAllDepartments() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM departments", null);
    }

    public Cursor getTotalHoursByTeacher() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT teacher_id, SUM(lecture_hours) AS lecture_hours, SUM(lab_hours) AS lab_hours FROM teaching_hours GROUP BY teacher_id", null);
    }

    public Cursor getTeachersWithMinDisciplines(int minDisciplines) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT teachers.id AS id, teachers.name AS name, COUNT(teaching_hours.discipline_id) AS discipline_count " +
                "FROM teachers " +
                "JOIN teaching_hours ON teachers.id = teaching_hours.teacher_id " +
                "GROUP BY teachers.id " +
                "HAVING COUNT(teaching_hours.discipline_id) > " + minDisciplines;

        return db.rawQuery(query, null);
    }

    public Cursor getTeachersByDepartment() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT departments.name as department_name, teachers.name as teacher_name " +
                "FROM teachers " +
                "INNER JOIN departments ON teachers.department_id = departments.id " +
                "ORDER BY departments.name";
        return db.rawQuery(query, null);
    }

    public Cursor getTeachersByTotalDisciplines() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT teachers.id AS id, teachers.name AS name, COUNT(teaching_hours.discipline_id) AS total_disciplines " +
                "FROM teachers " +
                "JOIN teaching_hours ON teachers.id = teaching_hours.teacher_id " +
                "GROUP BY teachers.id " +
                "ORDER BY total_disciplines DESC";
        return db.rawQuery(query, null);
    }
}