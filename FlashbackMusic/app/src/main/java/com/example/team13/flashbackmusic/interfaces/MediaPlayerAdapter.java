package com.example.team13.flashbackmusic.interfaces;

/**
 * Created by Kazutaka on 3/11/18.
 */

public interface MediaPlayerAdapter {
    void loadMedia(int resourceId);

    void release();

    boolean isPlaying();

    void play();

    void reset();

    void pause();

    void initializeProgressCallback();

    void seekTo(int position);
}
