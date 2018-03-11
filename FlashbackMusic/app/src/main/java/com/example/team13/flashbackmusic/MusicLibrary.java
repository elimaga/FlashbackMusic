package com.example.team13.flashbackmusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import com.example.team13.flashbackmusic.interfaces.MusicLibraryObserver;
import com.example.team13.flashbackmusic.interfaces.Subject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kazutaka on 3/6/18.
 */

class MusicLibrary extends AsyncTask<String, Integer, Boolean> implements Subject<MusicLibraryObserver> {

    private Context mContext;
    private static MusicLibrary instance = null;
    private ArrayList<Album> albums = null;
    private ArrayList<Song> songs = null;
    private SharedPreferences sharedPreferences;
    private final String NUM_OF_ALBUMS_KEY = "num_of_albums";
    private final String SONG_METADATA_SET_KEY = "song_metadata_set";
    private final String ALBUM_METADATA_SET_KEY = "album_metadata_set";
    private ArrayList<DatabaseMediator> mediators = null;

    // these sets store metadata to check if a object is made from the file already.
    private ArrayList<HashMap<String, String>> songMetadataSet = null;
    private ArrayList<HashMap<String, String>> albumMetadataSet= null;

    ArrayList<MusicLibraryObserver> observers;

    // This is Singleton class, call getInstance() instead
    private MusicLibrary(Context context) {
        mContext = context;
        albums = new ArrayList<>();
        songs = new ArrayList<>();
        mediators = new ArrayList<>();
        songMetadataSet = new ArrayList<>();
        albumMetadataSet = new ArrayList<>();
        observers = new ArrayList<>();
        sharedPreferences = mContext.getSharedPreferences("music_library", MODE_PRIVATE);
        loadSongObjects();
        loadMetadataSet();
    }

    private MusicLibrary(MusicLibrary musicLibrary) {
        mContext = musicLibrary.mContext;
        albums = musicLibrary.albums;
        songs = musicLibrary.songs;
        songMetadataSet = musicLibrary.songMetadataSet;
        albumMetadataSet = musicLibrary.albumMetadataSet;
        observers = musicLibrary.observers;
        sharedPreferences = mContext.getSharedPreferences("music_library", MODE_PRIVATE);
    }

    @Override
    protected Boolean doInBackground(String ... args) {
        List<String> argList = Arrays.asList(args);
        ArrayList<String> paths = new ArrayList<>(argList.subList(0,argList.size()-2));
        String url = argList.get(argList.size() - 1);
        addSongsIntoLibraryFromPath(paths, url);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        persistUnsavedAlbums();
        persistMetadataSet();
        notifyObservers(new Intent());
    }

    static public MusicLibrary getInstance(Context context) {
        if (instance == null) {
            instance = new MusicLibrary(context);
        }
        return instance;
    }

    ArrayList<Song> getSongs() { return songs; }

    ArrayList<Album> getAlbums() { return albums; }

    void addSongsIntoLibraryFromPath(ArrayList<String> paths, String url) {
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
                        songMetadata.get("track"),
                        url,
                        songs.size(),
                        album.getAlbumName());
                song.setPath(path);
                songs.add(song);
                DatabaseMediator mediator = new DatabaseMediator(song);
                mediators.add(mediator);
                album.addSong(song);
                songMetadataSet.add(songMetadata);
            }
        }
        return;
    }

    public void updateLibraryInBackground(ArrayList<String> paths, String url) {
        ArrayList<String> argArrayList = paths;
        argArrayList.add(url);
        instance = new MusicLibrary(this);
        instance.execute(argArrayList.toArray(new String[0]));
    }

    // this function takes care of the case trying to add duplicated songs.
    void addSongsFromFirebase(ArrayList<Song> newSongs) {
        for (Song song : songs) {
            // todo check duplicate
        }
        persistUnsavedAlbums();
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
        map.put("artist", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
        map.put("albumName", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        map.put("track", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
        return map;
    }

    private void persistUnsavedAlbums() {
        // Save the info in the SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int numOfStoredAlbums = sharedPreferences.getInt(NUM_OF_ALBUMS_KEY, 0);
        for (int i = numOfStoredAlbums; i < albums.size(); i++ ) {
            Album album = albums.get(i);
            persistAlbum(album);
        }
        editor.putInt(NUM_OF_ALBUMS_KEY, albums.size());
        editor.apply();
    }

    private void persistAlbum(Album album) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(album);
        editor.putString(Integer.toString(album.getIndex()), json);
        editor.apply();
    }

    private void loadSongObjects() {
        int numOfStoredAlbums = sharedPreferences.getInt(NUM_OF_ALBUMS_KEY, 0);
        for (int i = 0; i < numOfStoredAlbums; i++) {
            String json = sharedPreferences.getString(Integer.toString(i), "");
            Gson gson = new Gson();
            Album album = gson.fromJson(json, Album.class);
            albums.add(album);
            for (Song song : album.getSongs()) {
                songs.add(song);
            }
        }
    }

    private void persistMetadataSet() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String songMetadataSetJson = gson.toJson(songMetadataSet);
        String albumMetadataSetJson = gson.toJson(albumMetadataSet);
        editor.putString(SONG_METADATA_SET_KEY, songMetadataSetJson);
        editor.putString(SONG_METADATA_SET_KEY, albumMetadataSetJson);
        editor.apply();
    }

    private void loadMetadataSet() {
        String songMetadataSetJson = sharedPreferences.getString(SONG_METADATA_SET_KEY, null);
        String albumMetadataSetJson = sharedPreferences.getString(ALBUM_METADATA_SET_KEY, null);
        Gson gson = new Gson();
        if (songMetadataSetJson != null) {
            songMetadataSet = gson.fromJson(songMetadataSetJson, (new ArrayList<HashMap<String, String>>()).getClass());
        }
        if (albumMetadataSetJson != null) {
            albumMetadataSet = gson.fromJson(albumMetadataSetJson, (new ArrayList<HashMap<String, String>>()).getClass());
        }
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
