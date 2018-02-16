package com.example.team13.flashbackmusic;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by andrewyu on 2/14/18.
 */

public class SongActivityPrepper {
    private Intent activityIntent;
    private Intent playerIntent;
    private ArrayList<Song> songs;

    public SongActivityPrepper(Intent activityIntent, Intent playerIntent, ArrayList<Song> songs)
    {
        this.activityIntent = activityIntent;
        this.playerIntent = playerIntent;
        this.songs = songs;
    }

    public void sendInfo()
    {
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> artists = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        double[] latitudes = new double[songs.size()];
        double[] longitudes = new double[songs.size()];
        ArrayList<String> times = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        ArrayList<Integer> resourceIds = new ArrayList<>();

        for(int index = 0; index < songs.size(); index++) {

            titles.add(songs.get(index).getTitle());
            artists.add(songs.get(index).getArtist());
            albums.add(songs.get(index).getAlbumName());
            latitudes[index] = songs.get(index).getLastLatitude();
            longitudes[index] = songs.get(index).getLastLongitude();
            times.add(songs.get(index).getLastTime());
            days.add(songs.get(index).getLastDay());
            indices.add(songs.get(index).getIndex());
            resourceIds.add(songs.get(index).getResId());

        }

        activityIntent.putExtra("titles", titles);
        activityIntent.putExtra("artists", artists);
        activityIntent.putExtra("albums", albums);
        activityIntent.putExtra("latitudes", latitudes);
        activityIntent.putExtra("longitudes", longitudes);
        activityIntent.putExtra("times", times);
        activityIntent.putExtra("days", days);
        activityIntent.putExtra("indices", indices);
        playerIntent.putExtra("resourceIds", resourceIds);

    }
}
