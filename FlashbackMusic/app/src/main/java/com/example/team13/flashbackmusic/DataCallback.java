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
    FBMUser user;
    Context context;
    Activity mainActivity;
    int numSongsQueried;
    int numSongsCalledBack;

    public DataCallback(ArrayList<Song> allSongs, VibeModePlaylist vibeModePlaylist, FBMUser user,
                        Context context, Activity mainActivity) {
        this.allSongs = allSongs;
        this.vibeModePlaylist = vibeModePlaylist;
        this.user = user;
        this.context = context;
        this.mainActivity = mainActivity;
        this.numSongsQueried = 0;
        this.numSongsCalledBack = 0;
    }

    public void callback(DatabaseEntry data){
        Log.d("Number of Songs Queried: ", "" + numSongsQueried);

        boolean songIsDownloaded = false;

        Log.d("Song Title: ", data.getTitle());

        for(int index = 0; index < allSongs.size(); index++) {
            Song song = allSongs.get(index);
            if (song.getTitle().equals(data.getTitle()) && song.getArtist().equals(data.getArtist())) {
                Log.d("Trying to add song titled: ", song.getTitle());
                Song newSong = new Song(data.getTitle(), data.getArtist(), data.getAlbumName(), data.getTrackNumber(),
                        data.getURL(), index, data.getLastDay(), data.getLastTime(), data.getLastLatitude(),
                        data.getLastLongitude(), data.getUsername(), data.getUserId(), data.getLastDate());
                newSong.setFavoriteStatus(song.getFavoriteStatus());

                if(vibeModePlaylist.addSong(newSong)) {
                    Log.d("Song added", "Setting data.");
                    if(user.checkIfFriend(newSong.getLastUserId())) {
                        song.setDataWithoutNotify(data.getLastLatitude(), data.getLastLongitude(), data.getLastDay(),
                                data.getLastTime(), data.getLastDate(), data.getUsername(), data.getUserId());
                    }
                    else {
                        String proxyname = user.createProxyName(data.getUsername(), data.getUserId());
                        song.setDataWithoutNotify(data.getLastLatitude(), data.getLastLongitude(), data.getLastDay(),
                                data.getLastTime(), data.getLastDate(), proxyname, data.getUserId());
                    }

                }
                songIsDownloaded = true;
                break;
            }
        }

        if(!songIsDownloaded) {
            //TODO: Check if the album is already created, if so, just create the song object with that album, if not then create a new album
            Song newSong = new Song(data.getTitle(), data.getArtist(), data.getAlbumName(), data.getTrackNumber(),
                    data.getURL(), allSongs.size(), data.getLastDay(), data.getLastTime(), data.getLastLatitude(),
                    data.getLastLongitude(), data.getUsername(), data.getUserId(), data.getLastDate());
            allSongs.add(newSong);
            vibeModePlaylist.addSong(newSong);
            //TODO: Download the song
        }

        numSongsCalledBack++;
        Log.d("Number of songs called back: ", "" + numSongsCalledBack);

        if(numSongsCalledBack == numSongsQueried) {
            vibeModePlaylist.sortPlaylist();

            // Only activate vibe mode if there are songs to play
            if (!vibeModePlaylist.getPlaylist().isEmpty()) {
                ArrayList<Integer> songIndices = new ArrayList<>();
                for(Song song : vibeModePlaylist.getPlaylist()){
                    Log.d("Song Title: ", "" + song.getTitle());
                    Log.d("Song Index: ", "" + song.getIndex());
                    Log.d("Song lastUsername: ", song.getLastUserName());
                    songIndices.add(song.getIndex());
                }
                //Play the vibeModePlaylist
                Intent intent = new Intent(context, SongActivity.class);
                intent.putExtra("songIndices", songIndices);
                intent.putExtra("vibeModeOn",true);
                intent.putExtra("username", user.getName());
                intent.putExtra("userId", user.getID());
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
