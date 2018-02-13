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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    final int INVALID_COORDINATE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        final int[] resourceIds = this.listRaw();

        ArrayList<String> songNames = loadSongs(mediaMetadataRetriever, resourceIds);


        //flashback button

        final Context context = getApplicationContext();
        ImageButton flashBackButton = findViewById(R.id.flashback_button);
        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: transition flashback mode activity!!
                // TODO: Please remove the following line
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                double[] userLocation = getLocation();
                String userTime = getTime();
                String userDay = getDay();

                // Create playlist object

                // Play the playlist
                //Song song = new Song("America Religious", "unknown", "Love Is Everywhere",
                //        "albums/loveiseverywhere/america-religious.mp3", "01/10", 0);
                //song.setData(0.0, 0.0, "Monday", "1:48");
                //playSong(song);

            }
        });
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
    public ArrayList<String> loadSongs(MediaMetadataRetriever mmr, int[] resourceIds)
    {
        //Return list of song (names)
        ArrayList<String> songsList = new ArrayList<>();

        for (int i = 0; i < resourceIds.length; i++)
        {
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(resourceIds[i]);
            mmr.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String trackNumber = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);

            System.out.println(title);

            //Create Song object
        }

        /*
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
        */

        return null;
    }

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
                != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission ( this ,
                Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions ( this ,
                    new  String[]{Manifest.permission.ACCESS_FINE_LOCATION },  REQUEST_LOCATION );
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null)
        {
            double[] newLocation = {location.getLatitude(), location.getLongitude()};
            return newLocation;
        }
        else {
            double[] newLocation = {INVALID_COORDINATE, INVALID_COORDINATE};
            return newLocation;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void playSong(Song song)
    {
        Intent intent = new Intent(this, SongActivity.class);
        String title = song.getTitle();
        String artist = song.getArtist();
        String album = song.getAlbumName();
        double latitude = song.getLastLatitude();
        double longitude = song.getLastLongitude();
        String time = song.getLastTime();
        String day = song.getLastDay();
        int resId = song.getResId();
        int index = song.getIndex();
        intent.putExtra("title", title);
        intent.putExtra("artist", artist);
        intent.putExtra("album", album);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("time", time);
        intent.putExtra("day", day);
        intent.putExtra("resId", resId);
        intent.putExtra("index", index);

        startActivityForResult(intent, 0);

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

            int index = extras.getInt("index");
            // can just get the Song object from the array right here and then get title
            // String title = songs[index].getTitle();
            // DOn't forget to remove "title" extra from SongActivity

            String title = (String) extras.get("title");
            double newLatitude = (double) extras.getDouble("newLatitude");
            double newLongitude = (double) extras.getDouble("newLongitude");
            String newDay = (String) extras.getString("newDay");
            String newTime = (String) extras.getString("newTime");
            System.out.println(newLatitude + " " + newLongitude + " " + newDay + " " + newTime);

            // Save the info in the SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("flashback", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(title + "_latitude", "" + newLatitude);
            editor.putString(title + "_longitude", "" + newLongitude);
            editor.putString(title + "_day", newDay);
            editor.putString(title + "_time", newTime);

            editor.apply();

            // Update the info for this song, need arraylist of songs first
            // songs[index].setData(newLatitude, newLongitude, newDay, newTime);

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

        System.out.println(newLatitude + " " + newLongitude + " " + newDay + " " + newTime);

    }


}

