package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Kazutaka on 2/6/18.
 */

public class SongTabFragment extends Fragment {

    private MainActivity main;
    private ListView songListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        main = (MainActivity) getActivity();
        View rootView = inflater.inflate(R.layout.song_tab_fragment, container, false);

        songListView = rootView.findViewById(R.id.song_list_view);
        songListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Get the song we arve going to play and add it to an ArrayList of songs to be played
                Song song = (Song) adapterView.getItemAtPosition(position);
                ArrayList<Song> songs = new ArrayList<>();
                songs.add(song);

                // Create the Intents to send the information and start the new activities
                Intent activityIntent = new Intent(main, SongActivity.class);
                Intent playerIntent = new Intent(main, PlayService.class);
                SongActivityPrepper songActivityPrepper = new SongActivityPrepper(activityIntent, playerIntent, songs);
                songActivityPrepper.sendInfo();

                // Stop any previous song from playing, start playing the songs, and display the info to the user
                main.stopService(playerIntent);
                main.startService(playerIntent);
                main.startActivityForResult(activityIntent, 0);


            }
        });


        SongAdapter songAdapter = new SongAdapter(main, main.getSongs());
        songListView.setAdapter(songAdapter);


        return rootView;

    }
}
