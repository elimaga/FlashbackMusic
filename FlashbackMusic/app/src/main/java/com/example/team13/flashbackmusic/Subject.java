package com.example.team13.flashbackmusic;

/**
 * Created by Kazutaka on 2/28/18.
 */

public interface Subject {
    void registerObserver(MusicFileManagerObserver observer);
    void removeObserver(MusicFileManagerObserver observer);
    void notifyObservers();
}

