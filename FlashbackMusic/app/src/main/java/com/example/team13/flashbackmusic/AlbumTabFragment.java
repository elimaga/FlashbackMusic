package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
    private MusicLibrary musicLibrary;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        main = (MainActivity) getActivity();
        musicLibrary = MusicLibrary.getInstance(main);

        View rootView =  inflater.inflate(R.layout.album_tab_fragment, container, false);

        albumListView = rootView.findViewById(R.id.album_list_view);
        albumListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Album album = (Album) adapterView.getItemAtPosition(position);
                ArrayList<Song> songsInAlbum = album.getSongs();
                ArrayList<Integer> indexOfSong = new ArrayList<>();

                // Only play the songs that are not disliked
                for(Song song : songsInAlbum) {
                    if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {
                        indexOfSong.add(song.getIndex());
                    }
                    else {
                        Log.d("Disliked Song", "Skipping");
                    }
                }

                // Only play the songs that are not empty
                if(!indexOfSong.isEmpty()) {
                    Intent intent = new Intent(main, SongActivity.class);
                    intent.putExtra("songIndices",indexOfSong);
                    intent.putExtra("vibeModeOn",false);
                    intent.putExtra("username", main.usr.getName());
                    intent.putExtra("userId", main.usr.getID());
                    main.startActivityForResult(intent, 0);
                }


            }
        });


        AlbumAdapter albumAdapter = new AlbumAdapter(main, musicLibrary.getAlbums());

        albumListView.setAdapter(albumAdapter);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        albumListView.setAdapter(new AlbumAdapter(main, musicLibrary.getAlbums()));
    }
}
