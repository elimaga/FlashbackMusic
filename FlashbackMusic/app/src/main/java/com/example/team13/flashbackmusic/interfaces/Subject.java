package com.example.team13.flashbackmusic.interfaces;

/**
 * Created by Kazutaka on 2/28/18.
 */

public interface Subject<T> {
    void registerObserver(T observer);
    void removeObserver(T observer);
    void notifyObservers();
}

