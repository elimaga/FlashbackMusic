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

                Album album = (Album) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(main, SongActivity.class);
                ArrayList<Song> songsInAlbum = album.getSongs();

                for(int index = songsInAlbum.size() - 1; index >= 0; index--) {

                    SongActivityPrepper songActivityPrepper = new SongActivityPrepper(intent, songsInAlbum.get(index));
                    songActivityPrepper.sendInfo();
                    main.startActivityForResult(intent, 0);

                }
            }
        });


        AlbumAdapter albumAdapter = new AlbumAdapter(main, main.getAlbums());

        albumListView.setAdapter(albumAdapter);


        return rootView;
    }
}
