package com.example.team13.flashbackmusic;

/*
 * Created by rolandkong and luzannebatoon on 2/6/18.
 */


import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String title, artist, lastDay, lastTime, setting, lastDate, path;
    private double lastLatitude, lastLongitude;
    private int track;
    private int index; // index in the ArrayList of Songs
    private FavoriteStatus favoriteStatus; // neutral = 0, like = 1, dislike = 2
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

    public Song(String title, String artist, Album album, String track, int index) {
        this.title = title;
        this.artist = artist;
        this.track = Integer.parseInt(track.substring(0, track.indexOf("/")));
        this.index = index;
        this.path = "";
        this.album = album;
        album.addSong(this);

    }

    private Song(String title, String artist, Album album, int track, int index) {
        this.title = title;
        this.artist = artist;
        this.track = track;
        this.index = index;
        this.path = "";
        this.album = album;

        album.addSong(this);
    }

    public Song()
    {
        this.title = "";
        this.artist = "";
        this.album = null;
        this.track = 0;
        this.index = 0;
        this.path = "";
    }

    protected Song(Parcel in) {
        title = in.readString();
        artist = in.readString();
        album = in.readParcelable(Album.class.getClassLoader());
        track = in.readInt();
        index = in.readInt();

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

    public String getPath() { return this.path; }

    public Album getAlbum() { return this.album; }



    public boolean isDownloaded() { return !(this.path.equals("")); }

    public void setPath(String path) { this.path = path; }

    public void setAlbum(Album album) {
        this.album = album;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeParcelable(album, 0);
        dest.writeInt(track);
        dest.writeInt(index);

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
