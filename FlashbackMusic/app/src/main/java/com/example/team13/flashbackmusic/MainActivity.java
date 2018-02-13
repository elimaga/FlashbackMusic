package com.example.team13.flashbackmusic;

import android.Manifest;
import android.content.Context;

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

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;

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
        final PagerAdapter pagerAdapter = new PagerAdapter( getSupportFragmentManager(), tabLayout.getTabCount());
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
                playSong();
            }
        });



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
            double[] newLocation = {100, 200};
            return newLocation;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void playSong()//Song song)
    {
        Intent intent = new Intent(this, SongActivity.class);
        /*
        String name = song.getName();
        String artist = song.getArtist();
        String album = song.getAlbum();
        String location = song.getLocation();
        String time = song.getTime();
        String day = song.getDay();
        String path = song.getPath();
        intent.putExtra("name", name);
        intent.putExtra("artist", artist);
        intent.putExtra("album", album);
        intent.putExtra("location", location);
        intent.putExtra("time", time);
        intent.putExtra("day", day);
        intent.putExtra("path", path);
        */

        intent.putExtra("path", "albums/loveiseverywhere/america-religious.mp3");
        startActivity(intent);
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            //String newLocation = (String) extras.getString("newLocation");
            //String newDay = (String) extras.getString("newDay");
            //String newTime = (String) extras.getString("newTime");

            //song.setLocation(newLocation);
            //song.setDay(newDay);
            //song.setTime(newTime);
        }


    }
}


