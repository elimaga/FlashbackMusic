package com.example.team13.flashbackmusic;

import android.util.Log;

import com.example.team13.flashbackmusic.interfaces.Callback;

import java.util.ArrayList;

/**
 * Created by Eli on 3/11/2018.
 */

public class SimpleCallback implements Callback {
    int numSongsQueried;
    int numSongsCalledBack;

    public SimpleCallback() {
        this.numSongsQueried = 0;
        this.numSongsCalledBack = 0;
    }


    @Override
    public void callback(DatabaseEntry data) {
        numSongsCalledBack++;
        Log.d("Number of Total Songs to be in Playlist: ", "" + numSongsQueried);
        Log.d("Number of songs in playlist right now: ", "" + numSongsCalledBack);
    }

    public void callback(ArrayList<DatabaseEntry> data) {
        for(DatabaseEntry d : data) {
            numSongsCalledBack++;
            Log.d("Number of Total Songs to be in Playlist: ", "" + numSongsQueried);
            Log.d("Number of songs in playlist right now: ", "" + numSongsCalledBack);
        }
    }

    public void incrNumSongsQueried()
    {
        numSongsQueried++;
    }
}
