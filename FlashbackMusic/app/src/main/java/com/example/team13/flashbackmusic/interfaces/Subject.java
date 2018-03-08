package com.example.team13.flashbackmusic.interfaces;

import android.content.Intent;

import com.example.team13.flashbackmusic.Unzipper;

/**
 * Created by Kazutaka on 2/28/18.
 */

public interface Subject<T> {
    void registerObserver(T observer);
    void removeObserver(T observer);
    void notifyObservers(Intent intent);
}

