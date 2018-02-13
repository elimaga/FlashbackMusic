package com.example.team13.flashbackmusic;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Kazutaka on 2/6/18.
 */

public class AlbumTabFragment extends Fragment {

    private ListView albumListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.album_tab_fragment, container, false);

        albumListView = rootView.findViewById(R.id.album_list_view);

        int capacity = 50;
        final ArrayList<Album> albumsList = new ArrayList<Album>(capacity);

        //create album object and stores as a list
        for (int i = 0; i < capacity; i++){
            Album album =  new Album("Divide","Ed Sheeran", 2);
            album.addSong(new Song("Shape of You",
                                    "Ed sheeran",
                                    "Divide",
                                    0,
                                    "1/2", i));
            album.addSong(new Song("Shape of You",
                    "Ed sheeran",
                    "Divide",
                    0,
                    "2/2", i));
            albumsList.add(album);
        }

        AlbumAdapter albumAdapter = new AlbumAdapter(getActivity(), albumsList);

        albumListView.setAdapter(albumAdapter);


        return rootView;
    }
}
