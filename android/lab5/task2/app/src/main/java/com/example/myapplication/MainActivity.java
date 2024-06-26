package com.example.myapplication;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity {

    TextView tvOut;
    TextView tvLon;
    TextView tvLat;
    Button startButton;
    Button stopButton;

    LocationManager mlocManager;
    LocationListener mlocListener;

    private StringBuilder gpxData;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gpxData = new StringBuilder();
        gpxData.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        gpxData.append("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"YourAppName\">\n");
        gpxData.append("<trk>\n");
        gpxData.append("<trkseg>\n");

        tvOut = findViewById(R.id.textView1);
        tvLon = findViewById(R.id.longitude);
        tvLat = findViewById(R.id.latitude);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mlocListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                tvLat.setText("Latitude: " + location.getLatitude());
                tvLon.setText("Longitude: " + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationUpdates();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
            }
        });

        // Check for location permissions at runtime if targeting Android 12
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions granted, start requesting location updates
            requestLocationUpdates();
        }
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
    }

    private void stopLocationUpdates() {
        mlocManager.removeUpdates(mlocListener);
        saveGPXFile();

    }
    private void saveGPXFile() {
        gpxData.append("</trkseg>\n");
        gpxData.append("</trk>\n");
        gpxData.append("</gpx>");

        try {
            File gpxFile = createFile();
            FileWriter writer = new FileWriter(gpxFile);
            writer.append(gpxData.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFile() throws IOException {
        File folder = Environment.getExternalStorageDirectory(); // Получаем корневую директорию внешнего хранилища
        String fileName = "route.gpx"; // Имя файла GPX
        return new File(folder, fileName);
    }




    private void updateGPXFile(Location location) {
        if (location != null) {
            gpxData.append("<trkpt lat=\"").append(location.getLatitude())
                    .append("\" lon=\"").append(location.getLongitude()).append("\">\n");
            gpxData.append("<ele>").append(location.getAltitude()).append("</ele>\n");
            gpxData.append("<time>").append(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date(location.getTime()))).append("</time>\n");
            gpxData.append("</trkpt>\n");
        }
    }

    private void updateUI(Location location) {
        // Update UI with location data
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start requesting location updates
                requestLocationUpdates();
            } else {
                // Permission denied, handle accordingly
                // For example, display a message or disable location-related functionality
            }
        }
    }
}
