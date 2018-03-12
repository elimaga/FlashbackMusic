package com.example.team13.flashbackmusic;

/**
 * Created by Eli on 3/11/2018.
 */

public class Callback {
    private boolean locationQueried;
    private boolean dateQueried;
    private boolean friendsQueried;

    public Callback() {
        locationQueried = false;
        dateQueried = false;
        friendsQueried = false;
    }

    public boolean queriesFinished() {
        return locationQueried && dateQueried && friendsQueried;
    }
}
