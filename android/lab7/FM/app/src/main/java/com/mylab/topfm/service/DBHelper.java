package com.mylab.topfm.service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mylab.topfm.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    final static int DB_VERSION = 1;
    final String LOG_TAG = "myLogs";
    public DBHelper(Context context) {

        super(context, "TopTracks", null, DB_VERSION);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table Tracks ("
                + "id integer primary key autoincrement,"
                +    "artist Text,"
                +    "name Text,"
                +    "playcount integer,"
                +    "listeners integer,"
                + "rank integer"+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addToDB(JSONArray top_tracks){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM Tracks where artist='" +
                    top_tracks.getJSONObject(0).getJSONObject("artist").getString("name") + "'");
                for (int i = 0 ; i < 10; i ++){
                    JSONObject track = null;
                    track = top_tracks.getJSONObject(i);
                    String name = track.getString("name");
                    String t_artist = track.getJSONObject("artist").getString("name");
                    int listeners = Integer.parseInt(track.getString("listeners"));
                    int playcount = Integer.parseInt(track.getString("playcount"));
                    int rank = Integer.parseInt(track.getJSONObject("@attr").getString("rank"));
                    ContentValues cv = new ContentValues();
                    cv.put("name", name);
                    cv.put("artist",  t_artist);
                    cv.put("listeners", listeners);
                    cv.put("playcount", playcount);
                    cv.put("rank", rank);
                    db.insert("Tracks", null, cv);
                }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Track> getTopTenTracksByArtist(String artist){

        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from Tracks where artist='" + artist + "' Order by rank Asc limit 10";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Track> tracks = new ArrayList<Track>();
        while(cursor.moveToNext()){
            Track track = new Track();
            track.setId(cursor.getInt(0));
            track.setArtist(cursor.getString(1));
            track.setName(cursor.getString(2));
            track.setPlaycount(cursor.getInt(3));
            track.setListeners(cursor.getInt(4));
            track.setRank(cursor.getInt(5));
            Log.d(LOG_TAG, "Id: "+track.getId() + " Name: " + track.getName() + " artist: "
                    + track.getArtist() + " Playcount: " + track.getPlaycount() + " Listeners: "
                    + track.getListeners() + " Rank: " + track.getRank());
            tracks.add(track);
        }
        cursor.close();
        db.close();
        return tracks;
    }

}
