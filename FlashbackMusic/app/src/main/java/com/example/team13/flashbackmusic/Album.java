package com.example.team13.flashbackmusic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Luzanne on 2/11/18.
 */

public class Album implements Parcelable {
    private String albumName, artist;
    private int index;
    private ArrayList<Song> songs;

    public Album (String albumName, String artist, String track, int index) {
        this.albumName = albumName;
        this.artist = artist;
        this.index = index;
        int numTracks = Integer.parseInt(track.substring(track.indexOf("/") + 1, track.length()));
        songs = new ArrayList<> ();
        for (int i = 0; i < numTracks; i++)
        {
            songs.add(new Song());
        }
    }

    protected Album(Parcel in) {
        albumName = in.readString();
        artist = in.readString();
        index = in.readInt();
        songs = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);

            }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getAlbumName() {
        return this.albumName;
    }

    public String getArtist() {
        return this.artist;
    }

    public ArrayList<Song> getSongs() {
        return this.songs;
    }

    public int getIndex() { return this.index; }

    public void addSong(Song s) {
        // Adds song at correct track index
        songs.set(s.getTrack() - 1, s);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumName);
        dest.writeString(artist);
        dest.writeInt(index);
        dest.writeTypedList(songs);

    }
}
