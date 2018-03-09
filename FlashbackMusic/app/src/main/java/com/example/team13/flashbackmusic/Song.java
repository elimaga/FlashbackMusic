package com.example.team13.flashbackmusic;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */

import java.util.ArrayList;

public class Song implements SongSubject{

    private String title, artist, albumName, lastDay, lastTime, setting, lastDate;
    private double lastLatitude, lastLongitude;
    private int resId;
    private int track;
    private int index; // index in the ArrayList of Songs
    private FavoriteStatus favoriteStatus; // neutral = 0, like = 1, dislike = 2
    private ArrayList<SongObserver> observers;

    public enum FavoriteStatus {
        NEUTRAL, LIKED, DISLIKED;
    }

    public Song(String title, String artist, String albumName,
                int id, String track, int index) {
        this.title = title;
        this.artist = artist;
        this.albumName = albumName;
        this.resId = id;
        this.track = Integer.parseInt(track.substring(0, track.indexOf("/")));
        this.index = index;
        this.observers = new ArrayList<>();
    }

    public Song()
    {
        this.title = "";
        this.artist = "";
        this.albumName = "";
        this.resId = 0;
        this.track = 0;
        this.index = 0;
        this.observers = new ArrayList<>();
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

    public void setFavoriteStatus(FavoriteStatus status) {
        this.favoriteStatus = status;
    }

    public FavoriteStatus getFavoriteStatus() {
        return this.favoriteStatus;
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


    /**
     * Method to set the location, date, and time data for the song. Calls notifyObservers() since
     * the data is being changed.
     * @param lastLatitude - the latitude that the song was played at
     * @param lastLongitude - the longitude the song was played at
     * @param day - the day of the week the song was played on
     * @param time - the time the song was played at
     * @param date - the date the song was played on
     */
    public void setData(double lastLatitude, double lastLongitude,
                         String day, String time, String date) {
        this.lastDay = day;
        this.lastTime = time;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.lastDate = date;
        setTimeOfDay(time);
        notifyObservers();
    }


    /**
     * Method to get the time of day (Morning, Afternoon, or Evening)
     * @param time - the time the song was played
     */
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

    /**
     * Method to add observers to the song object
     * @param observer - the observer to add
     */
    public void registerObserver(SongObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Method to notify the observers that the data in the song has been changed.
     */
    public void notifyObservers() {
        for(SongObserver observer : observers) {
            observer.update();
        }
    }

}
