package com.example.team13.flashbackmusic;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by andrewyu on 2/14/18.
 */

public class SongActivityPrepper {
    private Intent intent;
    private ArrayList<Song> songs;

    public SongActivityPrepper(Intent i, ArrayList<Song> s)
    {
        this.intent = i;
        this.songs = s;
    }

    public void sendInfo()
    {

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> artists = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        double[] latitudes  = new double[songs.size()];
        double[] longitudes  = new double[songs.size()];
        ArrayList<String> times = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<Integer> resIds = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        for(int index = 0; index < songs.size(); index++) {

            titles.add(songs.get(index).getTitle());
            String artist = song.getArtist();
            String album = song.getAlbumName();
            double latitude = song.getLastLatitude();
            double longitude = song.getLastLongitude();
            String time = song.getLastTime();
            String day = song.getLastDay();
            int resId = song.getResId();
            int index = song.getIndex();

        }
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
