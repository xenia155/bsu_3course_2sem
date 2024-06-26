package com.example.task3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView latitudeText;
    private TextView longitudeText;
    private TextView altitudeText;
    private TextView startedText;
    private TextView elapsedText;

    private Button startButton;
    private Button stopButton;
    private static MainActivity act;

    public static final String GPS_ACTION = "by.bsu.dektiarev.pms_lab5.GPS_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeText = findViewById(R.id.latitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        startedText = findViewById(R.id.startedText);
        elapsedText = findViewById(R.id.elapsedText);
        altitudeText = findViewById(R.id.altitudeText);
        startButton = findViewById(R.id.buttonStart);
        stopButton = findViewById(R.id.buttonStop);


        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(GPS_ACTION), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        startButton.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, pendingIntent);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.removeUpdates(pendingIntent);
                    Snackbar.make(v, "Please turn on GPS", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Pending...", Toast.LENGTH_LONG).show();
            } catch (SecurityException ex) {
                Snackbar.make(v, "Check permissions error", Snackbar.LENGTH_LONG).show();
            }

            final Handler handler = new Handler();

            Thread timeThread = new Thread(() -> {
                final Calendar calendar = Calendar.getInstance();
                handler.post(() -> startedText.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) +
                        ":" + calendar.get(Calendar.SECOND)));
                long startTime = SystemClock.currentThreadTimeMillis();

                while (true) {
                    final long time = SystemClock.currentThreadTimeMillis() - startTime;
                    handler.post(() -> elapsedText.setText(String.valueOf(time)));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            timeThread.start();
        });

        stopButton.setOnClickListener(v -> {
            try {
                locationManager.removeUpdates(pendingIntent);
            } catch (SecurityException ex) {
                Snackbar.make(v, "Check permissions error", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}