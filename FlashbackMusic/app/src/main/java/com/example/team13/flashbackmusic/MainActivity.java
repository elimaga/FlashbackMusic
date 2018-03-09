package com.example.team13.flashbackmusic;


import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;

import android.Manifest;
import android.content.Context;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.Calendar;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    ImageButton flashBackButton;

    private ArrayList<Song> songs;
    private ArrayList<Album> albums;
    private ArrayList<DatabaseMediator> mediators;

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    final int INVALID_COORDINATE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load the songs and albums into the ArrayLists
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        final int[] resourceIds = this.listRaw();
        loadLibrary(mediaMetadataRetriever, resourceIds);

        //Tab layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("SONG"));
        tabLayout.addTab(tabLayout.newTab().setText("ALBUM"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // Flashback button
        final Context context = getApplicationContext();
        flashBackButton = findViewById(R.id.flashback_button);
        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                double[] userLocation = UserInfo.getLocation(MainActivity.this, locationManager);
                String userTime = UserInfo.getTime();
                String userDay = UserInfo.getDay();
                String userDate = UserInfo.getDate();

                // Generate the Flashback Playlist
                FlashbackPlaylist flashbackPlaylist = new FlashbackPlaylist(songs, userLocation,
                        userDay, userTime, userDate);
                ArrayList<Song> playlist = flashbackPlaylist.getPlaylist();

                // Only activate flashback mode if there are songs to play
                if (!playlist.isEmpty()) {
                    // Play the playlist
                    Intent intent = new Intent(MainActivity.this, SongActivity.class);
                    SongActivityPrepper songActivityPrepper = new SongActivityPrepper(intent, playlist);
                    songActivityPrepper.sendInfo(true);
                    startActivityForResult(intent, 1);
                }
                Log.d("Flashback Button", "Flashback button is pressed from main activity");

            }
        });

        // Requesting location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        // Testing retrieve methods
        Song song = new Song();
        DatabaseMediator databaseMediator = new DatabaseMediator(song);
        databaseMediator.retrieveSongsByLocation(49.0, 25.0);
        databaseMediator.retrieveSongsByDate("3/6/18");

        ArrayList<String> friends = new ArrayList<>();
        friends.add("usr1");
        databaseMediator.retrieveSongsByFriend(friends);

        // TODO: Fix this so we can actually get the list of queried songs
        ArrayList<String> data = databaseMediator.getQueriedSongs();
        for (String d : data){
            System.out.println(d);
        }
    }

    public Song getSong(int index)
    {
        return songs.get(index);
    }

    public Album getAlbum(int index)
    {
        return albums.get(index);
    }

    public ArrayList<Song> getSongs() {return songs;}

    public ArrayList<Album> getAlbums() {return albums;}


    /**
     * loadLibrary() reads the mp3 files in the raw folder and returns a
     * fully constructed list of Songs and Albums
     * @param mmr - MediaMetadataRetriever that will retrieve various metadata from an mp3 file
     * @param resourceIds - the ids for the mp3 files in the raw folder
     */
    public void loadLibrary(MediaMetadataRetriever mmr, int[] resourceIds)
    {
        // initialize songs and albums lists
        songs = new ArrayList<>();
        albums = new ArrayList<>();
        mediators = new ArrayList<>();

        // loop through each mp3 file
        for (int i = 0; i < resourceIds.length; i++)
        {
            // get the metadata from the files
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(resourceIds[i]);
            mmr.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String trackNumber = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);

            Song song = new Song(title, artist, albumName, resourceIds[i], trackNumber, i);
            retrieveInfo(song);
            songs.add(song);

            // Add mediators to each song to listen for changes in the songs
            DatabaseMediator mediator = new DatabaseMediator(song);
            song.registerObserver(mediator);
            mediators.add(mediator);



            if(albums.isEmpty()) {
                Album album = new Album(albumName, artist, trackNumber);
                album.addSong(song);
                albums.add(album);
            }
            else {

                boolean needNewAlbum = true;


                for (Album album : albums) {
                    if(album.getAlbumName().equals(albumName)) {
                        album.addSong(song);
                        needNewAlbum = false;
                        break;
                    }
                }

                if(needNewAlbum) {
                    Album album = new Album(albumName, artist, trackNumber);
                    album.addSong(song);
                    albums.add(album);
                }
            }
        }
    }

    /**
     * Helper method to get the resource ids for all the raw files.
     * @return int[] - the array holding all the resource ids for the mp3 files in raw folder
     */
    private int[] listRaw()
    {
        Field[] fields = R.raw.class.getFields();
        int[] resourceIds = new int[fields.length];
        for (int i = 0; i < fields.length; i++)
        {
            try
            {
                resourceIds[i] = fields[i].getInt(fields[i]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return resourceIds;
    }



    /**
     * When our song activity finishes, this method is called and stores all the new data for the
     * song in the shared preferences. This data has to be retrieved to store in the Song object
     * after playSong() finishes.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();

        // If the songs ended normally or back was pressed while in default mode then simply update
        // the info of the songs.
        if (requestCode == 0 && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) && data != null) {
            updateSongs(extras);
        }
        // Else if back button was pressed in Flashback Mode then update the songs that were played,
        // but do not restart Flashback Mode
        else if (requestCode == 1 && resultCode == RESULT_CANCELED && data != null) {
            updateSongs(extras);
        }
        // Else if the songs ended normally in Flashback Mode then update the songs and restart
        // flashback mode
        else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            updateSongs(extras);
            flashBackButton.performClick();
        }
    }

    /**
     * Method to retrieve the prior info of the song from the SharedPreferences and then set the
     * data in the Song object.
     * @param song - the song to retrieve the info for
     */
    public void retrieveInfo(Song song) {

        String title = song.getTitle();

        //Get the info from the new info from the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("flashback", MODE_PRIVATE);
        double latitude = Double.parseDouble(sharedPreferences.getString(title + "_latitude", "" + INVALID_COORDINATE));
        double longitude = Double.parseDouble(sharedPreferences.getString(title + "_longitude", "" + INVALID_COORDINATE));
        String day = sharedPreferences.getString(title + "_day", "");
        String time = sharedPreferences.getString(title + "_time", "");
        String date = sharedPreferences.getString(title + "_date", "");
        Song.FavoriteStatus favoriteStatus = Song.FavoriteStatus.valueOf(sharedPreferences.getString(title + "_favStatus", "NEUTRAL"));


        // Update the data in the Song object
        song.setData(latitude, longitude, day, time, date);
        song.setFavoriteStatus(favoriteStatus);

    }

    /**
     * Method to update the new location, date, and time info for the songs that have been played
     * @param extras - contains the new data for the songs
     */
    private void updateSongs(Bundle extras) {
        // Save the info in the SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("flashback", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ArrayList<Integer> indices = extras.getIntegerArrayList("indices");
        ArrayList<String> newLatitudes = extras.getStringArrayList("newLatitudes");
        ArrayList<String> newLongitudes = extras.getStringArrayList("newLongitudes");
        ArrayList<String> newTimes = extras.getStringArrayList("newTimes");
        ArrayList<String> newDays = extras.getStringArrayList("newDays");
        ArrayList<String> newDates = extras.getStringArrayList("newDates");

        for (int index = 0; index < indices.size(); index++) {

            String title = songs.get(indices.get(index)).getTitle();
            double newLatitude = Double.parseDouble(newLatitudes.get(index));
            double newLongitude = Double.parseDouble(newLongitudes.get(index));
            String newDay = newDays.get(index);
            String newTime = newTimes.get(index);
            String newDate = newDates.get(index);

            editor.putString(title + "_latitude", "" + newLatitude);
            editor.putString(title + "_longitude", "" + newLongitude);
            editor.putString(title + "_day", newDay);
            editor.putString(title + "_date", newDate);
            editor.putString(title + "_time", newTime);

            editor.apply();

            songs.get(indices.get(index)).setData(newLatitude, newLongitude, newDay, newTime, newDate);

        }
    }




}

