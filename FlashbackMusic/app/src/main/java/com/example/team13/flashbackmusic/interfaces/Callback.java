package com.example.team13.flashbackmusic.interfaces;

import com.example.team13.flashbackmusic.DatabaseEntry;

import java.util.ArrayList;

/**
 * Created by Eli on 3/11/2018.
 */

public interface Callback {
    void callback(DatabaseEntry data);
    void callback(ArrayList<DatabaseEntry> data);
    void incrNumSongsQueried();
}
