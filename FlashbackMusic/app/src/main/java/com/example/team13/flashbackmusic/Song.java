package com.example.team13.flashbackmusic;

import android.location.Location;
import java.sql.Array;
import java.sql.Time;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */

/* SONG CLASS */
public class Song {
    private String title, artist, albumName, path, track, lastTime, setting;
    private double lastLatitude, lastLongitude;
    private int liked; // neutral = 0, dislike = -1, like = 1

    // What type to store location and time?
//    private Location lastLocation;
//    private Time lastTime;

    public Song(String title, String artist, String albumName, String duration,
                String path, String track) {
        this.title = title;
        this.artist = artist;
        this.albumName = albumName;
        this.path = path;
        this.track = track;
        this.liked = 0;
    }

    // Isn't playing the song handled by the activity?
    //public void playSong() {
    // Fake methods. How to get time and location?
    //lastLocation = getLocation();
    //lastTime = getTime();
    //mediaPlayer.start();
    //}

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public String getPath() {
        return this.path;
    }

    public String getTrack() { return this.track; }

    public void likeSong(int like) {
        this.liked = like;
    }

    private double getLastLatitude() {
        return this.lastLatitude;
    }

    private double getLastLongitude() {
        return this.lastLongitude;
    }

    private String getLastTime() {
        return this.lastTime;
    }

    private void setData(double lastLatitude, double lastLongitude,
                         String time) {
        this.lastTime = time;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;

        int hour = Integer.parseInt(time.substring(0, time.indexOf((":"))));
        if ((hour >= 5) && (hour < 11))
            this.setting = "Morning";
        else if ((hour >=11) && (hour < 17))
            this.setting = "Afternoon";
        else
            this.setting = "Evening";
    }

}

/* END OF SONG CLASS */
