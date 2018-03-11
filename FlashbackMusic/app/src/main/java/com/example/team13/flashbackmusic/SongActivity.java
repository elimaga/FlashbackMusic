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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SongActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;
    final int INVALID_COORDINATE = 200;
    int index = 0;
    Bundle extras;
    Button playPauseButton;
    ArrayList<Integer> resIds;
    ArrayList<Integer> indices;
    ArrayList<String> newLatitudes;
    ArrayList<String> newLongitudes;
    ArrayList<String> newTimes;
    ArrayList<String> newDays;
    ArrayList<String> newDates;

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
        newDates = new ArrayList<>();
        boolean vibeModeOn = false;

        if(extras != null) {
            resIds = extras.getIntegerArrayList("resIds");
            vibeModeOn = extras.getBoolean("vibeModeOn");
        }

        // If Flashback Mode is on, display it to the user so they know they are in flashback mode
        TextView VibeMode = (TextView) findViewById(R.id.vibe_mode);
        if(vibeModeOn) {
            VibeMode.setText("Vibe Mode");
        }

        // Update the screen for the first song, load the first song to play, and update the Arraylists
        // of new data since the song is playing.
        updateScreen();
        loadMedia(resIds.get(index));
        updateNewData();


        // Play the song when it is prepared
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
                    Log.d("Testing Playback", "Song is not playing");
                }
            }
        });

        // Play the next song if there is one, otherwise finish the activity
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
                    sendDataBack(false);
                    finish();
                }
            }
        });

        playPauseButton = (Button) findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMusic();
            }
        });

    }

    /**
     * Stops the music and and activity when the back button is pressed. Sends the new location/date/time
     * data for the song back to the main activity.
     */
    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        sendDataBack(true);
        finish();
    }

    /**
     * Tests if the song is still playing when the home button or the menu button is pressed.
     * Note: This test assumes that the song is playing before the home button is pressed.
     */
    @Override
    protected void onUserLeaveHint() {
        if(mediaPlayer.isPlaying()) {
            Log.d("Testing Playback on Home or Menu Press", "Song is playing");
        }
        super.onUserLeaveHint();
    }


    /**
     * Method to load the media in the MediaPlayer to play the song.
     * @param resId - the resource id for the song that is to be played.
     */
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

    /**
     * Method to play and pause the music
     */
    private void toggleMusic() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setText("PLAY");
            Log.d("Testing Playback", "Song is paused");
        } else {
            mediaPlayer.start();
            playPauseButton.setText("PAUSE");
            Log.d("Testing Playback", "Song is playing");
        }
    }

    /**
     * Method to update the screen so the user knows what song is playing
     */
    private void updateScreen() {

        TextView songNameView = (TextView) findViewById(R.id.titleTextView);
        TextView songArtistView = (TextView) findViewById(R.id.artistTextView);
        TextView songAlbumView = (TextView) findViewById(R.id.albumTextView);
        TextView songLocationView = (TextView) findViewById(R.id.locationTextView);
        TextView songDateView = (TextView) findViewById(R.id.dateTextView);
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
            songDateView.setText("Date: " + (String) extras.getStringArrayList("dates").get(index));
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
                    Log.d("SongActivity.java", result);
                    songLocationView.setText("Location: " + result);
                }
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Method to update the new data ArrayLists to send back to the MainActivity.
     */
    private void updateNewData() {

        //get new location, day, and time
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double[] newLocation = UserInfo.getLocation(SongActivity.this, locationManager);

        String newDay = UserInfo.getDay();
        String newTime = UserInfo.getTime();
        String newDate = UserInfo.getDate();

        // add them and their song index to their appropriate arraylist
        indices.add(extras.getIntegerArrayList("indices").get(index));
        newLatitudes.add("" + newLocation[0]);
        newLongitudes.add("" + newLocation[1]);
        newDays.add(newDay);
        newTimes.add(newTime);
        newDates.add(newDate);
    }

    /**
     * Method to send data back to the MainActivity
     * @param backPressed - tells whether the back button was pressed to cancel playback or the song(s)
     *                    finished on their own
     */
    private void sendDataBack(boolean backPressed) {
        // put new location, day, and time in extras to send back to main activity
        Intent newData = new Intent();
        newData.putExtra("newLatitudes", newLatitudes);
        newData.putExtra("newLongitudes", newLongitudes);
        newData.putExtra("newTimes", newTimes);
        newData.putExtra("newDays", newDays);
        newData.putExtra("newDates", newDates);
        newData.putExtra("indices", indices);

        // Tell whether the back button is pressed or not
        if(backPressed) {
            setResult(Activity.RESULT_CANCELED, newData);
        }
        else {
            setResult(Activity.RESULT_OK, newData);
        }
    }
}

