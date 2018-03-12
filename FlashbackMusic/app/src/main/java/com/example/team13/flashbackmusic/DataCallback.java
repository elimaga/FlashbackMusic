package com.example.team13.flashbackmusic;

import com.example.team13.flashbackmusic.interfaces.Callback;

import java.util.ArrayList;

/**
 * Created by Eli on 3/11/2018.
 */

public class DataCallback implements Callback {

    ArrayList<Song> allSongs;
    VibeModePlaylist playlist;

    public DataCallback(ArrayList<Song> allSongs, VibeModePlaylist playlist) {
        this.allSongs = allSongs;
        this.playlist = playlist;
    }

    public void callback(DatabaseEntry data){
        boolean songIsDownloaded = false;

        for(Song song : allSongs) {
            if (song.getTitle().equals(data.getTitle()) && song.getArtist().equals(data.getArtist())) {
                playlist.addSong(song);
                songIsDownloaded = true;
                break;
            }
        }

        if(!songIsDownloaded) {
            Song newSong = new Song(data.getTitle(), data.getArtist(), data.getAlbumName(), data.getTrackNumber(),
                    data.getURL(), allSongs.size(), data.getLastDay(), data.getLastTime(), data.getLastLatitude(),
                    data.getLastLongitude(), data.getUsername(), data.getLastDate());
            allSongs.add(newSong);
            playlist.addSong(newSong);
            //TODO: Download the song
        }


    }
}
