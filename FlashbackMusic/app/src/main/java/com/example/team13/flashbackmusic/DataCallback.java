package com.example.team13.flashbackmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.team13.flashbackmusic.interfaces.Callback;

import java.util.ArrayList;

/**
 * Created by Eli on 3/11/2018.
 */

public class DataCallback implements Callback {

    ArrayList<Song> allSongs;
    VibeModePlaylist vibeModePlaylist;
    Context context;
    Activity mainActivity;
    int numSongsQueried;
    int numSongsCalledBack;

    public DataCallback(ArrayList<Song> allSongs, VibeModePlaylist vibeModePlaylist, Context context, Activity mainActivity) {
        this.allSongs = allSongs;
        this.vibeModePlaylist = vibeModePlaylist;
        this.context = context;
        this.mainActivity = mainActivity;
        this.numSongsQueried = 0;
        this.numSongsCalledBack = 0;
    }

    public void callback(DatabaseEntry data){
        Log.d("Number of Total Songs to be in Playlist: ", "" + numSongsQueried);

        boolean songIsDownloaded = false;

        for(int index = 0; index < allSongs.size(); index++) {
            if (allSongs.get(index).getTitle().equals(data.getTitle()) && allSongs.get(index).getArtist().equals(data.getArtist())) {
                Song song = new Song(data.getTitle(), data.getArtist(), data.getAlbumName(), data.getTrackNumber(),
                        data.getURL(), index, data.getLastDay(), data.getLastTime(), data.getLastLatitude(),
                        data.getLastLongitude(), data.getUsername(), data.getLastDate());
                vibeModePlaylist.addSong(song);
                songIsDownloaded = true;
                break;
            }
        }

        if(!songIsDownloaded) {
            //TODO: Check if the album is already created, if so, just create the song object with that album, if not then create a new album
            Song newSong = new Song(data.getTitle(), data.getArtist(), data.getAlbumName(), data.getTrackNumber(),
                    data.getURL(), allSongs.size(), data.getLastDay(), data.getLastTime(), data.getLastLatitude(),
                    data.getLastLongitude(), data.getUsername(), data.getLastDate());
            allSongs.add(newSong);
            vibeModePlaylist.addSong(newSong);
            //TODO: Download the song
        }

        numSongsCalledBack++;
        Log.d("Number of songs in vibeModePlaylist right now: ", "" + numSongsCalledBack);

        if(numSongsCalledBack == numSongsQueried) {
            // sort the vibeModePlaylist

            // Only activate vibe mode if there are songs to play
            if (!vibeModePlaylist.getPlaylist().isEmpty()) {
                ArrayList<Integer> songIndices = new ArrayList<>();
                for(Song song : vibeModePlaylist.getPlaylist()){
                    songIndices.add(song.getIndex());
                }
                //Play the vibeModePlaylist
                Intent intent = new Intent(context, SongActivity.class);
                intent.putExtra("songIndices",songIndices);
                intent.putExtra("vibeModeOn",true);
                mainActivity.startActivityForResult(intent, 1);
            }
        }

    }

    public void callback(ArrayList<DatabaseEntry> data) {
        for(DatabaseEntry d : data) {
            callback(d);
        }
    }

    public void incrNumSongsQueried()
    {
        numSongsQueried++;
    }
}
