package com.example.team13.flashbackmusic;

import java.util.ArrayList;

/**
 * Created by Luzanne on 2/11/18.
 */

public class Album {
    private String albumName, artist;
    private ArrayList<Song> songs;

    public Album (String albumName, String artist, String track) {
        this.albumName = albumName;
        this.artist = artist;
        int numTracks = Integer.parseInt(track.substring(track.indexOf("/") + 1, track.length()));
        songs = new ArrayList<> ();
        for (int i = 0; i < numTracks; i++)
        {
            songs.add(new Song());
        }
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public String getArtist() {
        return this.artist;
    }

    public ArrayList<Song> getSongs() {
        return this.songs;
    }

    public void addSong(Song s) {
        // Adds song at correct track index
        songs.add(s.getTrack() - 1, s);
        songs.remove(s.getTrack());

    }
}
