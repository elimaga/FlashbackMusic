package com.example.team13.flashbackmusic;

import android.content.Intent;

/**
 * Created by andrewyu on 2/14/18.
 */

public class SongActivityPrepper {
    private Intent intent;
    private Song song;

    public SongActivityPrepper(Intent i, Song s)
    {
        this.intent = i;
        this.song = s;
    }

    public void sendInfo()
    {
        String title = song.getTitle();
        String artist = song.getArtist();
        String album = song.getAlbumName();
        double latitude = song.getLastLatitude();
        double longitude = song.getLastLongitude();
        String time = song.getLastTime();
        String day = song.getLastDay();
        int resId = song.getResId();
        int index = song.getIndex();
        intent.putExtra("title", title);
        intent.putExtra("artist", artist);
        intent.putExtra("album", album);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("time", time);
        intent.putExtra("day", day);
        intent.putExtra("resId", resId);
        intent.putExtra("index", index);
    }
}
