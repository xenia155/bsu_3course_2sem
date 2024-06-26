package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView launchesCountTextView;
    EditText etMail, pswd;
    Button sign_in_btn;
    SharedPreferences pref;
    Intent intent;
    int launchesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        launchesCountTextView = findViewById(R.id.launchesCountTextView);
        etMail = findViewById(R.id.etEmail);
        pswd = findViewById(R.id.etPassword);
        sign_in_btn = findViewById(R.id.btnPlay);

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        launchesCount = pref.getInt("launches", 0);
        launchesCount++;
        launchesCountTextView.setText(("application launches number: " + launchesCount));
        intent = new Intent(MainActivity.this, GameActivity.class);

        if (pref.contains("username") && pref.contains("password")) {
            String savedUsername = pref.getString("username", "");
            String savedPassword = pref.getString("password", "");

            String enteredUsername = etMail.getText().toString();
            String enteredPassword = pswd.getText().toString();

            if (enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) {
                // Проверяем, совпадают ли введенные данные с сохраненными
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("launches", launchesCount);
                editor.apply();

                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish(); // Закрываем MainActivity, чтобы пользователь не мог к нему вернуться без авторизации
            } else {
                Toast.makeText(getApplicationContext(), "Credentials are not valid", Toast.LENGTH_SHORT).show();
            }
        }

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etMail.getText().toString();
                String password = pswd.getText().toString();

                if (username.equals("1") && password.equals("111")) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putInt("launches", launchesCount);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish(); // Закрываем MainActivity после успешной авторизации
                } else {
                    Toast.makeText(getApplicationContext(), "Credentials are not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}