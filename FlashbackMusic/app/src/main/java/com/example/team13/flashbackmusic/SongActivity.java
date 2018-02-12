package com.example.team13.flashbackmusic;

import android.Manifest;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SongActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    final int INVALID_COORDINATE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        TextView songNameView = (TextView) findViewById(R.id.titleTextView);
        TextView songArtistView = (TextView) findViewById(R.id.artistTextView);
        TextView songAlbumView = (TextView) findViewById(R.id.albumTextView);
        TextView songLocationView = (TextView) findViewById(R.id.locationTextView);
        TextView songDayView = (TextView) findViewById(R.id.dayTextView);
        TextView songTimeView = (TextView) findViewById(R.id.timeTextView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String path = "";
        double latitude = INVALID_COORDINATE;
        double longitude = INVALID_COORDINATE;

        if(extras != null)
        {

            songNameView.setText("Title: " + (String) extras.getString("title"));
            songArtistView.setText("Artist: " + (String) extras.getString("artist"));
            songAlbumView.setText("Album: " + (String) extras.getString("album"));
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            songDayView.setText("Day: " + (String) extras.getString("day"));
            songTimeView.setText("Time: " + (String) extras.getString("time"));


            path = (String) extras.getString("path");
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


        //get new location, day, and time
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double[] newLocation = getLocation();

        String newDay = getDay();
        String newTime = getTime();

        //System.out.println(newLocation[0] + " " + newLocation[1] + " " + newDay + " " + newTime);
        //put new location, day, and time in extras

        loadMedia(path);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //kills SongActivity when song finishes
                finish();
            }
        });

        final Button backButton = (Button) findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                finish();
            }
        });
    }

    public void loadMedia(String path)
    {
        if (mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
        }



        try {
            AssetFileDescriptor afd = this.getAssets().openFd(path);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepareAsync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        // Write your code here
        super.onDestroy();
        mediaPlayer.release();
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
}
