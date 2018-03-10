package com.example.team13.flashbackmusic;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */


import android.os.Parcel;
import android.os.Parcelable;

public class Song {

    private String title, artist, albumName, lastDay, lastTime, setting, lastDate, path, url;
    private double lastLatitude, lastLongitude;
    private int track;
    private int index; // index in the ArrayList of Songs
    private FavoriteStatus favoriteStatus; // neutral = 0, like = 1, dislike = 2


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
        this.path = "";
        this.albumName = albumName;
    }

    public Song()
    {
        this.title = "";
        this.artist = "";
        this.track = 0;
        this.url = "";
        this.index = 0;
        this.path = "";
        this.albumName = "";
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

    public String getPath() { return this.path; }

    public void setFavoriteStatus(FavoriteStatus status) {
        this.favoriteStatus = status;
    }


    public boolean isDownloaded() { return !(this.path.equals("")); }

    public void setPath(String path) { this.path = path; }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
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
