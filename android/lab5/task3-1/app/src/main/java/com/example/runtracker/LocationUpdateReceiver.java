package com.example.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class LocationUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
            // Получение обновления местоположения
            Location location = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
            if (location != null) {
                // Обработка местоположения (например, сохранение координат)
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Дополнительные действия по вашему усмотрению
            }
        }
    }
}