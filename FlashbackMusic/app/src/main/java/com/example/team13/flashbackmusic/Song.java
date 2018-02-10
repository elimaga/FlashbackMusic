package com.example.team13.flashbackmusic;

import android.location.Location;
import java.sql.Array;
import java.sql.Time;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */

/* SONG CLASS */
public class Song {
    private String title, artist, albumName, duration, path, track, lastTime, setting;
    private Location lastLocation;
    private int liked; // neutral = 0, dislike = -1, like = 1

    // What type to store location and time?
//    private Location lastLocation;
//    private Time lastTime;

    public Song(String title, String artist, String albumName, String duration, String path, String track) {
        this.title = title;
        this.artist = artist;
        this.albumName = albumName;
        this.duration = duration;
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

    public String getDuration() {
        long numduration = Long.parseLong(duration)/1000;
        long h = numduration /3600;
        long m = (numduration - h * 3600) / 60;
        long sec = numduration - (h * 3600 + m * 60);
        String durationText = "" + m + ":" + sec;
        return durationText;
    }

    public String getPath() {
        return this.path;
    }

    public String getTrack() { return this.track; }

    public void likeSong(int like) {
        this.liked = like;
    }

    private Location getLastLocation() {
        return this.lastLocation;
    }

    private String getLastTime() {
        return this.lastTime;
    }

    private void setData(Location location, String time) {
        this.lastTime = time;
        this.lastLocation = location;
        int hour = Integer.parseInt(time.substring(0, 2));
        if ((hour >= 5) && (hour < 11))
            this.setting = "Morning";
        else if ((hour >=11) && (hour < 17))
            this.setting = "Afternoon";
        else
            this.setting = "Evening";
    }

}

/* END OF SONG CLASS */
