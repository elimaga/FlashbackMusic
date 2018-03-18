package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private MusicLibrary musicLibrary;
    private PagerAdapter.SortKind sortKind;
    SongAdapter songAdapter;

    public void setSortKind(PagerAdapter.SortKind sortKind) {
        this.sortKind = sortKind;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        main = (MainActivity) getActivity();
        View rootView = inflater.inflate(R.layout.song_tab_fragment, container, false);
        musicLibrary = MusicLibrary.getInstance(main);

        songListView = rootView.findViewById(R.id.song_list_view);
        songListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Song song = (Song) adapterView.getItemAtPosition(position);
                ArrayList<Song> songToPlay = new ArrayList<>();
                ArrayList<Integer> indexOfSong = new ArrayList<>();

                // Only play the song if it's not disliked
                if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {
                    songToPlay.add(song);
                    indexOfSong.add(song.getIndex());
                    Log.d("Song Tab Fragment", "Song Title: " + song.getTitle());
                    Log.d("Song Tab Fragment", "Song Index: " + song.getIndex());

                }
                else {
                    Log.d("Disliked Song", "Skipping");
                }

                // Only play the song if it's not empty
                if(!songToPlay.isEmpty()) {

                    Intent intent = new Intent(main, SongActivity.class);
                    intent.putExtra("songIndices",indexOfSong);
                    intent.putExtra("vibeModeOn", false);
                    intent.putExtra("username", main.usr.getName());
                    intent.putExtra("userId", main.usr.getID());

                    main.startActivityForResult(intent, 0);
                }

            }
        });

        SongAdapter songAdapter = new SongAdapter(main, musicLibrary.getSongs(), "main");
        songListView.setAdapter(songAdapter);
        songAdapter.notifyDataSetChanged();
        songListView.refreshDrawableState();

        return rootView;

    }

    private ArrayList<Song> getSortedSongs() {
        ArrayList<Song> songs;
        switch (sortKind) {
            case TITLE:
                songs = SongSorter.sortByTitle(musicLibrary.getSongs());
                break;
            case ARTIST:
                songs = SongSorter.sortByArtist(musicLibrary.getSongs());
                break;
            case ALBUM:
                songs = SongSorter.sortByAlbum(musicLibrary.getAlbums());
                break;
            case FAVORITE:
                songs = SongSorter.extractFavorites(musicLibrary.getSongs());
                break;
            default:
                songs = SongSorter.sortByAlbum(musicLibrary.getAlbums());
                break;
        }
        return songs;
    }

    void refresh() {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                songListView.setAdapter(new SongAdapter(main, musicLibrary.getSongs(), "main"));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        songListView.setAdapter(new SongAdapter(main, musicLibrary.getSongs(), "main"));
    }
}
