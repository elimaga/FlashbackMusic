package com.example.team13.flashbackmusic;

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

    private ListView songListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.song_tab_fragment, container, false);

        songListView = rootView.findViewById(R.id.song_list_view);
        songListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // TODO: PLAY SONG HERE!!!!!!!!!!
                Song song = (Song) adapterView.getItemAtPosition(position);

            }
        });

        MainActivity main = (MainActivity)  getActivity();
        SongAdapter songAdapter = new SongAdapter(main, main.songs);
        songListView.setAdapter(songAdapter);


        return rootView;

    }
}
