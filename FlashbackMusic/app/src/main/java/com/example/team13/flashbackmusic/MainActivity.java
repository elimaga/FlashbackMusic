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

import java.util.ArrayList;
import java.util.Calendar;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> songs;
    private ArrayList<Album> albums;

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
        ImageButton flashBackButton = findViewById(R.id.flashback_button);
        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, FlashbackActivity.class);
                startActivity(intent);


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                double[] userLocation = getLocation();
                String userTime = getTime();
                String userDay = getDay();

                // Create playlist object

                // Play the playlist

                Log.d("Flashback Button", "Flashback button is pressed from main activity");

            }
        });

        // Requesting location permission
        if  (ActivityCompat.checkSelfPermission ( this , Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission ( this ,
                Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions ( this ,
                    new  String[]{Manifest.permission.ACCESS_FINE_LOCATION },  REQUEST_LOCATION );
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





    private String getDay()
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch(day)
        {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }

        return "";
    }

    private String getTime()
    {
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);//currentTime.getHours();
        int mins = Calendar.getInstance().get(Calendar.MINUTE);//currentTime.getMinutes();

        return hours + ":" + mins;
    }

    private double[] getLocation()
    {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if  (ActivityCompat.checkSelfPermission ( this , Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED  || ActivityCompat.checkSelfPermission ( this ,
                Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            // TODO: in addtion to checking null, the check for whether location service is on is needed here
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null)
            {
                double[] newLocation = {location.getLatitude(), location.getLongitude()};
                return newLocation;
            }

        }
        return new double[] {INVALID_COORDINATE, INVALID_COORDINATE};


    }


    /*
     * When our song activity finishes, this method is called and stores all the new data for the
     * song in the shared preferences. This data has to be retrieved to store in the Song object
     * after playSong() finishes.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();

            // Save the info in the SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("flashback", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            ArrayList<Integer> indices = extras.getIntegerArrayList("indices");
            ArrayList<String> newLatitudes = extras.getStringArrayList("newLatitudes");
            ArrayList<String> newLongitudes = extras.getStringArrayList("newLongitudes");
            ArrayList<String> newTimes = extras.getStringArrayList("newTimes");
            ArrayList<String> newDays = extras.getStringArrayList("newDays");


            for (int index = 0; index < indices.size(); index++) {

                String title = songs.get(indices.get(index)).getTitle();
                double newLatitude = Double.parseDouble(newLatitudes.get(index));
                double newLongitude = Double.parseDouble(newLongitudes.get(index));
                String newDay = newDays.get(index);
                String newTime = newTimes.get(index);


                editor.putString(title + "_latitude", "" + newLatitude);
                editor.putString(title + "_longitude", "" + newLongitude);
                editor.putString(title + "_day", newDay);
                editor.putString(title + "_time", newTime);

                editor.apply();

                songs.get(indices.get(index)).setData(newLatitude, newLongitude, newDay, newTime);

            }




        }
    }

    /*
     * Method to retrieve the prior info of the song from the SharedPreferences and then set the
     * data in the Song object.
     */
    private void retrieveInfo(Song song) {

        String title = song.getTitle();

        //Get the info from the new info from the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("flashback", MODE_PRIVATE);
        double newLatitude = Double.parseDouble(sharedPreferences.getString(title + "_latitude", "" + INVALID_COORDINATE));
        double newLongitude = Double.parseDouble(sharedPreferences.getString(title + "_longitude", "" + INVALID_COORDINATE));
        String newDay = sharedPreferences.getString(title + "_day", "");
        String newTime = sharedPreferences.getString(title + "_time", "");

        // Update the data in the Song object
        song.setData(newLatitude, newLongitude, newDay, newTime);

    }


}

