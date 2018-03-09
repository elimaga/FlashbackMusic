package com.example.team13.flashbackmusic;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class DatabaseEntry {

    private static final double INVALID_LOCATION = 200.0;

    private String title, artist, albumName, lastDay, lastTime, lastDate, url;
    private double lastLatitude, lastLongitude;
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
        //this.lastLocation = new ArrayList<>();
        this.lastLatitude = INVALID_LOCATION;
        this.lastLongitude = INVALID_LOCATION;
        this.track = 0;
        this.username = "";
    }


    //Public getters
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbumName() { return albumName; }
    public String getLastDay() { return lastDay; }
    public String getLastTime() { return lastTime; }
    public String getLastDate() { return lastDate; }
    public String getURL() { return url; }
    public double getLastLatitude() { return lastLatitude; }
    public double getLastLongitude() { return lastLongitude; }
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
    public void setLastLatitude(double newLatitude) { this.lastLatitude = newLatitude; }
    public void setLastLongitude(double newLongitude) { this.lastLongitude = newLongitude; }
    public void setTrackNumber(int newTrack) { this.track = newTrack; }
    public void setUsername(String newUsername) { this.username = newUsername; }
}
