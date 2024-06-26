package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnOpen;
    private TextView hiddenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpen = findViewById(R.id.btnOpen);
        hiddenTextView = findViewById(R.id.hiddenTextView);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformation();
            }
        });
    }

    private void showInformation() {
        String name = "Chernyshova Ksenia";
        String groupName = "12";
        String projectName = "First Project";

        String information = name + "\n" + groupName + "\n" + projectName;
        hiddenTextView.setText(information);
        hiddenTextView.setVisibility(View.VISIBLE);
    }
}