package com.example.team13.flashbackmusic;

import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        ArrayList<String> songNames = loadSongs(mediaMetadataRetriever);

        for(String title : songNames)
        {
            System.out.println(title);
        }
    }

    /*
    public ArrayList<String> loadAlbums(ArrayList<String> songs)
    {
        ArrayList<String> albums = new ArrayList<String>();

        if(albums.isEmpty())
        {
            // Create a new album object and add it to the ArrayList of albums
            Album next = new Album(songs.get(0).getArtist(), songs.get(0).getAlbum());
            albums.add(next);
        }

        int index = 0;

        for (String song : songs)
        {
            if (albums.get(index).getName().equals(song.getAlbum()))
            {
                albums.get(index).add(song);
            }
            else
            {
                // Create a new album object and add to the ArrayList
                Album next = new Album(song.getArtist(), song.getAlbum());
                next.add(song);
                albums.add(next);
                index++;
            }
        }

        for(Album album : albums)
        {
            sort(album);
        }

        return albums;
    }//*/

    /**
     * loadSongs() reads the mp3 files in the asset folder and returns a
     * fully constructed list of Songs.
     * @param mmr MediaMetadataRetriever that will retrieve various metadata from an mp3 file
     * @return Returns a list of song (names)
     * TODO: currently returns a list of song titles, once song class is finished return list of songs
     */
    public ArrayList<String> loadSongs(MediaMetadataRetriever mmr)
    {
        //Return list of song (names)
        ArrayList<String> songNameList = new ArrayList<>();

        try {
            //get all album names in asset folder
            String dir = "albums";
            String[] albumNameList = this.getAssets().list(dir);

            //iterate over all albums
            for (String album : albumNameList) {
                //get all song names in an album
                String albumPath = dir + "/" + album;
                String[] songs = this.getAssets().list(albumPath);
                //iterate over all songs
                for (String song : songs) {
                    //construct a file path to the song from the asset folder
                    String mp3File = albumPath + "/" + song;

                    //Create an AssetFileDescriptor to read in the song file
                    AssetFileDescriptor afd = this.getAssets().openFd(mp3File);
                    //Set the MediaMetadataRetriever to read from the song file specified in the
                    //AssetFileDescriptor, offsets are important for correct read-in
                    mmr.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

                    //Retrieve the song name
                    String name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    //Add song name to return list
                    songNameList.add(name);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //return song (names)
        return songNameList;
    }
}