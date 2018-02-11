package com.example.team13.flashbackmusic;

/**
 * Created by KM on 2/10/2018.
 * A simple class to create an album object
 */

public class Album {
    String albumTitle;
    String artistName;
    int numOfSong;

    public Album(String albumTitle, String artistName, int numOfSong){
        this.albumTitle = albumTitle;
        this.artistName = artistName;
        this.numOfSong = numOfSong;
    }
}
