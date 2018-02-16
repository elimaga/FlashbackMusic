package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Kazutaka on 2/6/18.
 */

public class AlbumTabFragment extends Fragment {

    MainActivity main;

    private ListView albumListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        main = (MainActivity) getActivity();

        View rootView =  inflater.inflate(R.layout.album_tab_fragment, container, false);

        albumListView = rootView.findViewById(R.id.album_list_view);
        albumListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Get the album we are going to play and its list of songs
                Album album = (Album) adapterView.getItemAtPosition(position);
                ArrayList<Song> songsInAlbum = album.getSongs();

                // Create the Intents to send the information and start the new activities
                Intent activityIntent = new Intent(main, SongActivity.class);
                Intent playerIntent = new Intent(main, PlayService.class);
                SongActivityPrepper songActivityPrepper = new SongActivityPrepper(activityIntent, playerIntent, songsInAlbum);
                songActivityPrepper.sendInfo();

                // Stop any previous song from playing, start playing the songs, and display the info to the user
                main.stopService(playerIntent);
                main.startService(playerIntent);
                main.startActivityForResult(activityIntent, 0);

            }
        });


        AlbumAdapter albumAdapter = new AlbumAdapter(main, main.getAlbums());

        albumListView.setAdapter(albumAdapter);


        return rootView;
    }
}
