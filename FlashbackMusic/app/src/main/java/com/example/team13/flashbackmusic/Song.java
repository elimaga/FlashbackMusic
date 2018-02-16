package com.example.team13.flashbackmusic;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */

import android.content.SharedPreferences;

public class Song {

    private String title, artist, albumName, lastDay, lastTime, setting, lastDate;
    private double lastLatitude, lastLongitude;
    private int resId;
    private int track;
    private int index; // index in the ArrayList of Songs
    private int liked; // neutral = 0, dislike = -1, like = 1

    public Song(String title, String artist, String albumName,
                int id, String track, int index) {
        this.title = title;
        this.artist = artist;
        this.albumName = albumName;
        this.resId = id;
        this.track = Integer.parseInt(track.substring(0, track.indexOf("/")));
        this.index = index;
        this.liked = 0;
    }

    public Song()
    {
        this.title = "";
        this.artist = "";
        this.albumName = "";
        this.resId = 0;
        this.track = 0;
        this.index = 0;
        this.liked = 0;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public int getResId() {
        return this.resId;
    }

    public int getTrack() { return this.track; }

    public int getIndex() { return this.index; }

    public void likeSong(int like) {
        this.liked = like;
    }

    public boolean isLiked() {
        return this.liked == 1;
    }

    public double getLastLatitude() {
        return this.lastLatitude;
    }

    public double getLastLongitude() {
        return this.lastLongitude;
    }

    public String getLastDay() {
        return this.lastDay;
    }

    public String getLastDate() {
        return this.lastDate;
    }


    public String getLastTime() {
        return this.lastTime;
    }

    public String getLastSetting() {
        return this.setting;
    }


    public void setData(double lastLatitude, double lastLongitude,
                         String day, String time, String date) {
        this.lastDay = day;
        this.lastTime = time;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.lastDate = date;
        setTimeOfDay(time);
    }

    private void setTimeOfDay(String time) {
        if(!time.equals("")) {

            int hour = Integer.parseInt(time.substring(0, time.indexOf((":"))));

            if ((hour >= 5) && (hour < 11))
                this.setting = "Morning";
            else if ((hour >= 11) && (hour < 17))
                this.setting = "Afternoon";
            else
                this.setting = "Evening";

        }
        else {
            this.setting = "";
        }
    }

}