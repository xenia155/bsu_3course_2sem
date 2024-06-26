package com.mylab.topfm.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mylab.topfm.Track;
import com.mylab.topfm.TrackAdapter;
import com.overwees.topfm.R;
import com.mylab.topfm.TrackViewModel;
import com.mylab.topfm.service.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Context context;
    Spinner spinner;
    ListView listView;
    String artist = "Rammstein";
    ArrayList<Track> trackList;
    TrackAdapter adapter;
    TrackViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        context = this;
        spinner = (Spinner)findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.listView);
        if(!isConnectingToInternet(getApplicationContext()))   {
            Toast.makeText(getApplicationContext(), "No internet access. Looking at internal saved data", Toast.LENGTH_LONG).show();
            trackList = dbHelper.getTopTenTracksByArtist(artist);
            adapter = new TrackAdapter(context,R.layout.track_activity, trackList);
            listView.setAdapter(adapter);
        } else{
            model = new ViewModelProvider(this).get(TrackViewModel.class);
            model.getValue(artist, context).observe((LifecycleOwner) context, value -> {
                adapter = new TrackAdapter(context,R.layout.track_activity, value);
                listView.setAdapter(adapter);
            });
            model.execute();
        }

        ArrayAdapter<CharSequence> artistsAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.artists,
                android.R.layout.simple_spinner_item
        );
        AdapterView.OnItemSelectedListener itemSelectedListenerArtists = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isConnectingToInternet(getApplicationContext()))
                    model.getValue(artist, context).observe((LifecycleOwner) context, value -> {
                        adapter = new TrackAdapter(context,R.layout.track_activity, value);
                        listView.setAdapter(adapter);
                    });
                artist = (String)parent.getItemAtPosition(position);
                trackList = dbHelper.getTopTenTracksByArtist(artist);
                adapter = new TrackAdapter(context,R.layout.track_activity, trackList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setAdapter(artistsAdapter);
        spinner.setOnItemSelectedListener(itemSelectedListenerArtists);


    }

    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;

    }
}