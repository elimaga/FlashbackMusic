package com.example.team13.flashbackmusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class DatabaseEntry {

    private static final double INVALID_LOCATION = 200.0;

    private String title, artist, albumName, lastDay, lastTime, lastDate, url;
    private List<Double> lastLocation;
    private int track;
    private String username;

    public DatabaseEntry()
    {

        this.title = "";
        this.artist = "";
        this.albumName = "";
        this.lastDay = "";
        this.lastTime = "";
        this.lastDate = "";
        this.url = "";
        this.lastLocation = new ArrayList<>();
        this.lastLocation.add(INVALID_LOCATION);
        this.lastLocation.add(INVALID_LOCATION);
        this.track = 0;
        this.username = "";

    }


    public DatabaseEntry(Song song, String url, String username)
    {
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.albumName = song.getAlbumName();
        this.lastDay = song.getLastDay();
        this.lastTime = song.getLastTime();
        this.lastDate = song.getLastDate();
        this.url = url;
        this.lastLocation = new ArrayList<>();
        this.lastLocation.add(song.getLastLatitude());
        this.lastLocation.add(song.getLastLongitude());
        this.track = song.getTrack();
        this.username = username;
    }

    //Public getters
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbumName() { return albumName; }
    public String getLastDay() { return lastDay; }
    public String getLastTime() { return lastTime; }
    public String getLastDate() { return lastDate; }
    public String getURL() { return url; }
    public List<Double> getLastLocation() { return lastLocation; }
    public int getTrackNumber() { return track; }
    public String getUsername() { return username; }

    //Public setters
    public void setTitle(String newTitle) { this.title = newTitle; }
    public void setArtist(String newArtist) { this.artist = newArtist; }
    public void setAlbumName(String newAlbum) { this.albumName = newAlbum; }
    public void setLastDay(String newLastDay) { this.lastDay = newLastDay; }
    public void setLastTime(String newLastTime) { this.lastTime = newLastTime; }
    public void setLastDate(String newLastDate) { this.lastDate = newLastDate; }
    public void setURL(String newURL) { this.url = newURL; }
    public void setLastLocation(List<Double> newLocation) { this.lastLocation = newLocation; }
    public void setTrackNumber(int newTrack) { this.track = newTrack; }
    public void setUsername(String newUsername) { this.username = newUsername; }
}
