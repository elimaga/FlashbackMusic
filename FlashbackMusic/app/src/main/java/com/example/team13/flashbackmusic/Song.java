package com.example.team13.flashbackmusic;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.team13.flashbackmusic.interfaces.SongObserver;
import com.example.team13.flashbackmusic.interfaces.SongSubject;


public class Song implements Parcelable, SongSubject {

    private String title, artist, lastDay, lastTime, setting, lastDate, path, url;
    private double lastLatitude, lastLongitude;
    private int track;
    private int index; // index in the ArrayList of Songs
    private FavoriteStatus favoriteStatus; // neutral = 0, like = 1, dislike = 2
    private ArrayList<SongObserver> observers;
    private Album album;

  
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

    public Song(String title, String artist, String track, String url, int index, Album album) {
        this.title = title;
        this.artist = artist;
        this.track = Integer.parseInt(track.substring(0, track.indexOf("/")));
        this.url = url;
        this.index = index;
        this.observers = new ArrayList<>();
        this.path = "";
        this.album = album;
        album.addSong(this);

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
        this.album = null;
    }

    protected Song(Parcel in) {
        title = in.readString();
        artist = in.readString();
        track = in.readInt();
        url = in.readString();
        index = in.readInt();
        album = in.readParcelable(Album.class.getClassLoader());
        lastLatitude = in.readDouble();
        lastLongitude = in.readDouble();
        lastDay = in.readString();
        lastTime = in.readString();
        lastDate = in.readString();
        path = in.readString();
        favoriteStatus = FavoriteStatus.fromInt(in.readInt());

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

    public Album getAlbum() { return this.album; }

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

    public void setAlbum(Album album) {
        this.album = album;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(artist);
        dest.writeInt(track);
        dest.writeString(url);
        dest.writeInt(index);
        dest.writeParcelable(album, 0);
        dest.writeDouble(lastLatitude);
        dest.writeDouble(lastLongitude);
        dest.writeString(lastDay);
        dest.writeString(lastTime);
        dest.writeString(lastDate);
        dest.writeString(path);
        dest.writeInt(favoriteStatus.toInt());
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
//        public Song(String title, String artist, Album album, String track, int index) {
            @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

}
