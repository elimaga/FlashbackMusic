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

        Bundle extras = getIntent().getExtras();
        String path = "";
        double latitude = INVALID_COORDINATE;
        double longitude = INVALID_COORDINATE;

        if(extras != null)
        {
            // TODO : display date of when it was last played
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
        double[] newLocation = UserInfo.getLocation(SongActivity.this);

        String newDay = UserInfo.getDay();
        String newTime = UserInfo.getTime();
        String newDate = UserInfo.getDate();

        //System.out.println(newLocation[0] + " " + newLocation[1] + " " + newDay + " " + newTime);
        // put new location, day, and time in extras
        Intent newData = new Intent();
        newData.putExtra("title", (String) extras.getString("title"));
        newData.putExtra("newLatitude", newLocation[0]);
        newData.putExtra("newLongitude", newLocation[1]);
        newData.putExtra("newTime", newTime);
        newData.putExtra("newDay", newDay);
        newData.putExtra("newDate", newDate);
        newData.putExtra("index", (int) extras.get("index"));
        setResult(Activity.RESULT_OK, newData);

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

}