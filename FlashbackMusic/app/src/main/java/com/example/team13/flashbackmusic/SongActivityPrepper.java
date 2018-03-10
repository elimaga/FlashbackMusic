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

    public void sendInfo(boolean flashbackModeOn)
    {

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> artists = new ArrayList<>();
        ArrayList<Album> albums = new ArrayList<>();
        double[] latitudes  = new double[songs.size()];
        double[] longitudes  = new double[songs.size()];
        ArrayList<String> times = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        for(int index = 0; index < songs.size(); index++) {

            titles.add(songs.get(index).getTitle());
            artists.add(songs.get(index).getArtist());
            albums.add(songs.get(index).getAlbum());
            latitudes[index] = songs.get(index).getLastLatitude();
            longitudes[index] = songs.get(index).getLastLongitude();
            times.add(songs.get(index).getLastTime());
            days.add(songs.get(index).getLastDay());
            dates.add(songs.get(index).getLastDate());
            indices.add(songs.get(index).getIndex());
            paths.add(songs.get(index).getPath());

        }

        intent.putExtra("titles", titles);
        intent.putExtra("artists", artists);
        intent.putExtra("albums", albums);
        intent.putExtra("latitudes", latitudes);
        intent.putExtra("longitudes", longitudes);
        intent.putExtra("times", times);
        intent.putExtra("days", days);
        intent.putExtra("dates", dates);
        intent.putExtra("paths", paths);
        intent.putExtra("indices", indices);
        intent.putExtra("flashbackModeOn", flashbackModeOn);

    }
}
