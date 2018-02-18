package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

                Song song = (Song) adapterView.getItemAtPosition(position);
                ArrayList<Song> songToPlay = new ArrayList<>();

                // Only play the song if it's not disliked
                if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {
                    songToPlay.add(song);
                }

                // Only play the song if it's not disliked
                if(!songToPlay.isEmpty()) {
                    Intent intent = new Intent(main, SongActivity.class);
                    SongActivityPrepper songActivityPrepper = new SongActivityPrepper(intent, songsToPlay);
                    songActivityPrepper.sendInfo();
                    main.startActivityForResult(intent, 0);
                }

            }
        });


        SongAdapter songAdapter = new SongAdapter(main, main.getSongs());
        songListView.setAdapter(songAdapter);


        return rootView;

    }
}
