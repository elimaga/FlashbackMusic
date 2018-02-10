package com.example.team13.flashbackmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        //songListView
        songListView = rootView.findViewById(R.id.song_list_view);
        int capacity = 50;
        final ArrayList<Song> songList = new ArrayList<Song>(capacity);

        for (int i = 0 ; i < capacity; i++){
            String duration = "2:30";
            Song newSong = new Song("Hello","Adele", duration);
            songList.add(newSong);
        }


        SongAdapter songAdapter = new SongAdapter(getActivity(), songList);

        songListView.setAdapter(songAdapter);


        return rootView;


    }
}
