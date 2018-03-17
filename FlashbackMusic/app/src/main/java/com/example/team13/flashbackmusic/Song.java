package com.example.team13.flashbackmusic;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */

import java.util.ArrayList;
import com.example.team13.flashbackmusic.interfaces.SongObserver;
import com.example.team13.flashbackmusic.interfaces.SongSubject;
import com.google.gson.annotations.SerializedName;


public class Song implements SongSubject {

    private String title, artist, albumName, lastDay, lastTime, setting, lastDate, lastUserName, lastUserId, path, url;
    private double lastLatitude, lastLongitude;
    private int track;
    private int index; // index in the ArrayList of Songs
    private FavoriteStatus favoriteStatus; // neutral = 0, like = 1, dislike = 2
    private transient ArrayList<SongObserver> observers;

    public enum FavoriteStatus {
        NEUTRAL, LIKED, DISLIKED;
        public Integer toInt() {
            switch(this) {
                case NEUTRAL:
                    return 0;
                case LIKED:
                    return 1;
                case DISLIKED:
                    return 2;
                default:
                    return 0;
            }
        }

        static public FavoriteStatus fromInt(int i) {
            switch(i) {
                case 0:
                    return NEUTRAL;
                case 1:
                    return LIKED;
                case 2:
                    return DISLIKED;
                default:
                    return NEUTRAL;
            }
        }
    }

    public Song(String title, String artist, String track, String url, int index, String albumName) {
        this.title = title;
        this.artist = artist;
        this.track = Integer.parseInt(track.substring(0, track.indexOf("/")));
        this.url = url;
        this.index = index;
        this.observers = new ArrayList<>();
        this.path = "";
        this.albumName = albumName;
        this.lastUserId = "";
    }

    public Song(String title, String artist, String album, int track, String url, int index,
                String lastDay, String lastTime, double lastLatitude, double lastLongitude,
                String lastUserName, String lastUserId , String lastDate) {
        this.title = title;
        this.artist = artist;
        this.track = track;
        this.url = url;
        this.index = index;
        this.lastDay = lastDay;
        this.lastTime = lastTime;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.lastUserName = lastUserName;
        this.lastUserId = lastUserId;
        this.lastDate = lastDate;
        this.albumName = album;
    }


    public Song()
    {
        this.title = "";
        this.artist = "";
        this.track = 0;
        this.url = "";
        this.index = 0;
        this.observers = new ArrayList<>();
        this.path = "";
        this.albumName = "";
        this.lastUserId = "";
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public int getTrack() { return this.track; }

    public String getUrl() { return this.url; }

    public int getIndex() { return this.index; }

    public String getAlbumName() { return this.albumName; }

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

    public String getLastUserName() { return this.lastUserName; }

    public String getLastUserId () { return this.lastUserId; }

    public String getPath() { return this.path; }

    public void setFavoriteStatus(FavoriteStatus status) {
        this.favoriteStatus = status;
    }

    public boolean isDownloaded() { return !(this.path.equals("")); }

    public void setPath(String path) { this.path = path; }

    // for safety purporse, it check it's from music library
    // not the best practice...
    void setIndex(int index, boolean isFromMusicLibrary) {
        if (isFromMusicLibrary) {
            this.index = index;
        }
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Method to set the location, date, and time data for the song. Calls notifyObservers() since
     * the data is being changed.
     * @param lastLatitude - the latitude that the song was played at
     * @param lastLongitude - the longitude the song was played at
     * @param day - the day of the week the song was played on
     * @param time - the time the song was played at
     * @param date - the date the song was played on
     * @param lastUserName - the name of the last user to play the song
     * @param lastUserId - the id of the last user to play the song
     */
    public void setData(double lastLatitude, double lastLongitude,
                        String day, String time, String date,
                        String lastUserName, String lastUserId) {
        this.lastDay = day;
        this.lastTime = time;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.lastDate = date;
        this.lastUserName = lastUserName;
        this.lastUserId = lastUserId;
        setTimeOfDay(time);
        notifyObservers();
    }

    /**
     * Method to set the location, date, and time data for the song without notifying the observers.
     * This is because we are updating the song with the data from Firebase, but the song has not
     * been played yet.
     * @param lastLatitude - the latitude that the song was played at
     * @param lastLongitude - the longitude the song was played at
     * @param day - the day of the week the song was played on
     * @param time - the time the song was played at
     * @param date - the date the song was played on
     * @param lastUserName - the name of the last user to play the song
     * @param lastUserId - the id of the last user to play the song
     */
    public void setDataWithoutNotify(double lastLatitude, double lastLongitude,
                        String day, String time, String date,
                        String lastUserName, String lastUserId) {
        this.lastDay = day;
        this.lastTime = time;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.lastDate = date;
        this.lastUserName = lastUserName;
        this.lastUserId = lastUserId;
        setTimeOfDay(time);
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
            observer.update(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        // If the object is a Song, check if the title and artist match
        if (o instanceof Song) {
            // If the title and artist match, return true
            if(this.getTitle().equals(((Song)o).getTitle()) && this.getArtist().equals(((Song)o).getArtist())) {
                return true;
            }
        }
        // Return false if o is not a Song or the titles/artists don't match
        return false;
    }

}
