package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import com.example.team13.flashbackmusic.interfaces.MusicLibraryObserver;
import com.example.team13.flashbackmusic.interfaces.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Kazutaka on 3/6/18.
 */

class MusicLibrary extends AsyncTask<String, Integer, Boolean> implements Subject<MusicLibraryObserver> {

    private static MusicLibrary instance = null;
    private ArrayList<Album> albums = null;
    private ArrayList<Song> songs = null;

    // these sets store metadata to check if a object is made from the file already.
    private ArrayList<HashMap<String, String>> songMetadataSet = null;
    private ArrayList<HashMap<String, String>> albumMetadataSet= null;

    ArrayList<MusicLibraryObserver> observers;

    // This is Singleton class, call getInstance() instead
    private MusicLibrary() {
        albums = new ArrayList<>();
        songs = new ArrayList<>();
        songMetadataSet = new ArrayList<>();
        albumMetadataSet = new ArrayList<>();
        observers = new ArrayList<>();

        loadDataFromSharedPref();
    }

    @Override
    protected Boolean doInBackground(String... paths) {
        addSongsIntoLibraryFromPath(new ArrayList<>(Arrays.asList(paths)));
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        notifyObservers(new Intent());
    }

    static public MusicLibrary getInstance() {
        if (instance == null) {
            instance = new MusicLibrary();
        }
        return instance;
    }

    ArrayList<Song> getSongs() { return songs; }

    ArrayList<Album> getAlbums() { return albums; }

    void addSongsIntoLibraryFromPath(ArrayList<String> paths) {
        for (String path : paths) {
            HashMap<String, String> songMetadata = retrieveSongMetadata(path);
            if (!(songMetadataSet.contains(songMetadata))) {
                HashMap<String, String> albumMetadata = extractAlbumMetadata(songMetadata);
                Album album;
                if (!(albumMetadataSet.contains(albumMetadata))) {
                    album = new Album(songMetadata.get("albumName"),
                            songMetadata.get("artist"),
                            songMetadata.get("track"),
                            albums.size());
                    albums.add(album);
                    albumMetadataSet.add(albumMetadata);
                } else {
                    album = albums.get(albumMetadataSet.indexOf(albumMetadata));
                }
                Song song = new Song(songMetadata.get("title"),
                        songMetadata.get("artist"),
                        album,
                        songMetadata.get("track"),
                        songs.size());
                songs.add(song);
                album.addSong(song);
                songMetadataSet.add(songMetadata);
            }
        }
        return;
    }

    // this function takes care of the case trying to add duplicated songs.
    void addSongsFromFirebase(ArrayList<Song> newSongs) {
        for (Song song : songs) {
            // todo check duplicate
        }
    }

    static private HashMap<String, String> extractAlbumMetadata(HashMap<String, String> songMetadata) {
        HashMap<String, String> albumMetadata = new HashMap<>();
        albumMetadata.put("artist", songMetadata.get("artist"));
        albumMetadata.put("albumName", songMetadata.get("albumName"));
        return albumMetadata;
    }

    static HashMap<String, String> retrieveSongMetadata(String path) {
        HashMap<String, String> map = new HashMap<>();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        map.put("title", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        map.put("artist", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        map.put("albumName", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        map.put("track", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
        return map;
    }

    private void loadDataFromSharedPref() {
        // TODO:
    }

    @Override
    public void registerObserver(MusicLibraryObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MusicLibraryObserver observer) {
        int index  = observers.indexOf(observer);
        if ( index > -1) observers.remove(index);
    }

    @Override
    public void notifyObservers(Intent intent) {
        for (MusicLibraryObserver observer : observers) {
            observer.onCompleteUpdate();
        }
    }
}
