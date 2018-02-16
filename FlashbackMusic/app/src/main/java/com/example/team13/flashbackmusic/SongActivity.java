package com.example.team13.flashbackmusic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SongActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    final int INVALID_COORDINATE = 200;
    int index = 0;
    Bundle extras;
    ArrayList<Integer> resIds;
    ArrayList<Integer> indices;
    ArrayList<String> newLatitudes;
    ArrayList<String> newLongitudes;
    ArrayList<String> newTimes;
    ArrayList<String> newDays;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        extras = getIntent().getExtras();
        indices = new ArrayList<>();
        newLatitudes = new ArrayList<>();
        newLongitudes = new ArrayList<>();
        newTimes = new ArrayList<>();
        newDays = new ArrayList<>();

        if(extras != null) {
            resIds = extras.getIntegerArrayList("resIds");
        }

        updateScreen();
        loadMedia(resIds.get(index));
        updateNewData();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                if (mediaPlayer.isPlaying())
                {
                    Log.d("Testing Playback", "Song is playing");
                    index++;
                }
                else
                {
                    Log.d("Testing Playback", "Song is playing");
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(index < resIds.size()) {
                    mediaPlayer.reset();
                    updateScreen();
                    loadMedia(resIds.get(index));
                    updateNewData();
                }
                else {
                    sendDataBack();
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        sendDataBack();
        finish();
    }

    public void loadMedia(int resId)
    {
        if (mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
        }



        try {
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(resId);
            mediaPlayer.setDataSource(afd);
            mediaPlayer.prepareAsync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
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

    /**
     * Method to update the screen so the user know what song is playing
     */
    private void updateScreen() {

        TextView songNameView = (TextView) findViewById(R.id.titleTextView);
        TextView songArtistView = (TextView) findViewById(R.id.artistTextView);
        TextView songAlbumView = (TextView) findViewById(R.id.albumTextView);
        TextView songLocationView = (TextView) findViewById(R.id.locationTextView);
        TextView songDayView = (TextView) findViewById(R.id.dayTextView);
        TextView songTimeView = (TextView) findViewById(R.id.timeTextView);

        double latitude = INVALID_COORDINATE;
        double longitude = INVALID_COORDINATE;

        if(extras != null)
        {

            songNameView.setText("Title: " + (String) extras.getStringArrayList("titles").get(index));
            songArtistView.setText("Artist: " + (String) extras.getStringArrayList("artists").get(index));
            songAlbumView.setText("Album: " + (String) extras.getStringArrayList("albums").get(index));
            latitude = extras.getDoubleArray("latitudes")[index];
            longitude = extras.getDoubleArray("longitudes")[index];
            songDayView.setText("Day: " + (String) extras.getStringArrayList("days").get(index));
            songTimeView.setText("Time: " + (String) extras.getStringArrayList("times").get(index));
        }

        // Shows the last location to the user
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            // Only want to show a location if we have a valid latitude and longitude
            if(latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180 )
            {

                List<Address> list = geocoder.getFromLocation(latitude, longitude, 1);
                if(list != null && list.size() > 0)
                {
                    Address address = list.get(0);
                    String result = address.getAddressLine(0);
                    songLocationView.setText("Location: " + result);

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void updateNewData() {

        //get new location, day, and time
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double[] newLocation = getLocation();
        String newDay = getDay();
        String newTime = getTime();

        // add them and their song index to their appropriate arraylist
        indices.add(extras.getIntegerArrayList("indices").get(index));
        newLatitudes.add("" + newLocation[0]);
        newLongitudes.add("" + newLocation[1]);
        newDays.add(newDay);
        newTimes.add(newTime);

    }

    private void sendDataBack() {
        // put new location, day, and time in extras to send back to main activity
        Intent newData = new Intent();
        newData.putExtra("newLatitudes", newLatitudes);
        newData.putExtra("newLongitudes", newLongitudes);
        newData.putExtra("newTimes", newTimes);
        newData.putExtra("newDays", newDays);
        newData.putExtra("indices", indices);
        setResult(Activity.RESULT_OK, newData);
    }
}
