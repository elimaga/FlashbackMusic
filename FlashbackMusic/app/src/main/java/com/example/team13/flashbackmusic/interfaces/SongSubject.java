package com.example.team13.flashbackmusic.interfaces;

import com.example.team13.flashbackmusic.interfaces.SongObserver;

/**
 * Created by Elijah Magallanes on 3/8/2018.
 */

public interface SongSubject {

    void registerObserver(SongObserver observer);
    void notifyObservers();

}
