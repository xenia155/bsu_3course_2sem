package com.mylab.topfm.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.XMLFormatter;

public class FMService extends AsyncTask {

    static final String LOG_TAG = "myLogs";
    static final String TOP_TEN_TRACKS = "https://ws.audioscrobbler.com/2.0/?method=artist.getTopTracks&artist=%s" +
            "&mbid=%s&api_key=31ba5f544ad3d53f04b38c682e3bfc99&format=json";;
    public static JSONObject getJSON(String artist,String mbid){
        try {
            URL url = new URL(String.format(TOP_TEN_TRACKS, artist, mbid));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
            {
                json.append(tmp).append("\n");
            }

            reader.close();

            JSONObject data = new JSONObject(json.toString());
            return data;
        }catch(Exception e){
            Log.d(LOG_TAG, "===="+e.getMessage()+"====");
            return null;
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
