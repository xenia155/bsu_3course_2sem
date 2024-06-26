package com.mylab.topfm;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mylab.topfm.service.DBHelper;
import com.mylab.topfm.service.FMService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrackViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Track>> value;
    private MutableLiveData<String> artist;
    private DBHelper dbHelper;
    private String[] artists = new String[]{"Rammstein", "Morgenstern", "Michael Jackson"};
    private String[] mbid = new String[]{"b2d122f9-eadb-4930-a196-8f221eeb0c66",
            "c3be5b94-b8fb-4617-bdaa-d543a7d57943",
            "f27ec8db-af05-4f36-916e-3d57f91ecf5e"};
    public LiveData<ArrayList<Track>> getValue(String _artist, Context _context) {
        if (value == null) {
            this.dbHelper = new DBHelper(_context);
        }
        this.artist = new MutableLiveData<String>(_artist);
        value = new MutableLiveData<ArrayList<Track>>(this.dbHelper.getTopTenTracksByArtist(_artist));
        return value;
    }

    public void execute(){

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    JSONObject obj;
                    for(int i = 0; i < 3; i++){
                        obj = FMService.getJSON(artists[i], mbid[i]);
                        workWithDB(obj);
                    }
                    value.postValue(value.getValue());
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
    }

    private void workWithDB(JSONObject json){
        try {
            JSONArray top = json.getJSONObject("toptracks").getJSONArray("track");
            dbHelper.addToDB(top);
        }catch(Exception e){
            Log.e("JSON error", "NO info");
        }
    }
}