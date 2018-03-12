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
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.Calendar;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    ImageButton vibeModeButton;
    private DrawerLayout mDrawerLayout;

    private ArrayList<Song> songs;
    private ArrayList<DatabaseMediator> mediators;
    private MusicLibrary musicLibrary;

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    final int INVALID_COORDINATE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicLibrary = MusicLibrary.getInstance(MainActivity.this);
        songs = musicLibrary.getSongs();

        setUpUI();

        SharedPreferences sharedPreferences = getSharedPreferences("vibe_mode",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("vibeModeOn",false)){
            vibeModeButton.performClick();
        }

        // Requesting location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        // Testing retrieve methods
        Song song = new Song();
        DatabaseMediator databaseMediator = new DatabaseMediator();
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

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

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

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        int id = menuItem.getItemId();
                        if(id == R.id.signout){
                            //signout Activity
                        }
                        else if (id == R.id.download){
                            //Bring up downloadActivity
                            Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                            startActivity(intent);

                        }
//                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        // VibeMode button
        final Context context = getApplicationContext();
        vibeModeButton = findViewById(R.id.flashback_button);
        vibeModeButton.setOnClickListener(new View.OnClickListener() {
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
        DatabaseMediator databaseMediator = new DatabaseMediator();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * When our song activity finishes, this method is called and stores all the new data for the
     * song in the shared preferences. This data has to be retrieved to store in the Song object
     * after playSong() finishes.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences sharedPreferences = getSharedPreferences("vibe_mode",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vibeModeOn", false);
        editor.apply();

        // if the songs ended normally in Flashback Mode then update the songs and restart
        // flashback mode
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            vibeModeButton.performClick();
        }
    }
}

