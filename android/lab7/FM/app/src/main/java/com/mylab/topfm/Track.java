package com.mylab.topfm;

import android.support.v4.os.IResultReceiver;

public class Track {

    int id;
    String name;
    String artist;
    int listeners;
    int playcount;
    int rank;

    public Track(int _id, String _name, String _artist, int _listeners, int _playcount, int _rank){
        setId(_id);
        setName(_name);
        setArtist(_artist);
        setListeners(_listeners);
        setPlaycount(_playcount);
        setRank(_rank);
    }

    public Track(){

    }

    public void setId(int _id){
        this.id = _id;

    }
    public void setName(String _name){
        this.name = _name;
    }
    public void setArtist(String _artist){
        this.artist= _artist;
    }
    public void setListeners(int _listeners){
        this.listeners = _listeners;
    }
    public void setPlaycount(int _playcount){
        this.playcount = _playcount;
    }
    public void setRank(int _rank){
        this.rank = _rank;
    }

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getArtist(){
        return this.artist;
    }
    public int getListeners(){
        return this.listeners;
    }
    public int getPlaycount(){
        return this.playcount;
    }
    public int getRank(){
        return this.rank;
    }
}
