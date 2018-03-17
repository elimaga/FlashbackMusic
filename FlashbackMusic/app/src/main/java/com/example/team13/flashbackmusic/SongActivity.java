package com.example.team13.flashbackmusic;

import android.support.constraint.ConstraintLayout;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SongActivity extends AppCompatActivity {

    private RelativeLayout dimLayout;
    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;
    final int INVALID_COORDINATE = 200;
    int index = 0;
    Bundle extras;
    Button playPauseButton;
    MusicLibrary musicLibrary;
    ArrayList<Integer> songIndices;
    Song currSong;
    PopupWindow popupWindow;
    SongAdapter adapter;
    View popupView;
    ConstraintLayout mainLayout;
    boolean vibeModeOn = false;
    String username = "";
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        dimLayout = (RelativeLayout) findViewById(R.id.dim_layout);
        Button trackPreviewButton = (Button) findViewById(R.id.trackPreviewButton);
        trackPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrackPreview();
            }
        });

        musicLibrary = MusicLibrary.getInstance(SongActivity.this);
        extras = getIntent().getExtras();

        if(extras != null) {
            songIndices = extras.getIntegerArrayList("songIndices");
            vibeModeOn = extras.getBoolean("vibeModeOn");
            username = extras.getString("username");
            userId = extras.getString("userId");
        }


        // Update the screen for the first song, and play the first song
        Log.d("Song Activity", "Playing song at index " + songIndices.get(0));
        currSong = musicLibrary.getSongs().get(songIndices.get(index));
        index++;

        updateScreen(currSong);

        playSong(currSong);

        setupMediaPlayer();

        setupUI();

        saveVibeState();

        mainLayout =
                (ConstraintLayout) findViewById(R.id.activity_song);
        final LayoutInflater inflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.track_preview_popup,
                (ViewGroup) findViewById(R.id.popup));
        final int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        final int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);

        final ListView listView =
                (ListView) popupView.findViewById(R.id.listView);
        adapter = new SongAdapter(this,
                musicLibrary.getSongsAtIndices(songIndices), "preview");
        listView.setAdapter(adapter);

    }

    private void saveVibeState() {
        // need to save whether the app was in vibe mode or not into shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("vibe_mode", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vibeModeOn", vibeModeOn);
        editor.apply();
    }

    private void setupMediaPlayer() {
        // Play the song when it is prepared
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                if (mediaPlayer.isPlaying())
                {
                    Log.d("Testing Playback", "Song is playing");
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
                updateSong(currSong);
                if(index < songIndices.size()) {
                    Log.d("More Songs to Play: ", songIndices.size() + " more songs.");
                    Song nextSong = musicLibrary.getSongs().get(songIndices.get(index));
                    mediaPlayer.reset();
                    updateScreen(nextSong);
                    playSong(nextSong);
                    currSong = nextSong;
                    index++;
                }
                else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void setupUI() {
        // If Flashback Mode is on, display it to the user so they know they are in flashback mode
        TextView VibeMode = (TextView) findViewById(R.id.vibe_mode);
        if(vibeModeOn) {
            VibeMode.setText("Vibe Mode");
        }

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
     * Method to load song into media player and play song
     * @param song - song object to play
     */
    public void playSong(Song song)
    {
        if (mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
        }
        try {
            File file = new File(song.getPath());
            Uri uri = Uri.fromFile(file);
            mediaPlayer.setDataSource(SongActivity.this, uri);
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
    private void updateScreen(Song song) {

        TextView songNameView = (TextView) findViewById(R.id.titleTextView);
        TextView songArtistView = (TextView) findViewById(R.id.artistTextView);
        TextView songAlbumView = (TextView) findViewById(R.id.albumTextView);
        TextView songLocationView = (TextView) findViewById(R.id.locationTextView);
        TextView songDateView = (TextView) findViewById(R.id.dateTextView);
        TextView songTimeView = (TextView) findViewById(R.id.timeTextView);
        TextView userNameTextView = (TextView) findViewById(R.id.userNameTextView);

        double latitude = song.getLastLatitude();
        double longitude = song.getLastLongitude();

        String songName = song.getTitle();
        String songArtist = song.getArtist();
        String albumName = song.getAlbumName();
        String date = song.getLastDate();
        String time = song.getLastTime();
        String user;
        boolean italics = false;
        if (song.getLastUserId().equals(userId))
        {
            user = "you";
            italics = true;
        }
        else
        {
            user = song.getLastUserName();
            italics = false;
        }

        if(extras != null) {
            songNameView.setText("Title: " + songName);
            songArtistView.setText("Artist: " + songArtist);
            songAlbumView.setText("Album: " + albumName);
            if(date != null) {
                songDateView.setText("Date: " + date);
            }
            if(time != null) {
                songTimeView.setText("Time: " + time);
            }
            userNameTextView.setText(user);
            if (italics)
            {
                userNameTextView.setTypeface(null, Typeface.ITALIC);
            }
            else
            {
                userNameTextView.setTypeface(null, Typeface.NORMAL);
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to set new data to a song
     */
    private void updateSong(Song song) {

        //get new location, day, and time
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double[] newLocation = UserInfo.getLocation(SongActivity.this, locationManager);

        String newDay = UserInfo.getDay();
        String newTime = UserInfo.getTime();
        String newDate = UserInfo.getDate();

        song.setData(newLocation[0], newLocation[1],newDay, newTime, newDate, username, userId);
        musicLibrary.persistSong(song);
    }

    private void openTrackPreview() {

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        dimLayout.setVisibility(View.VISIBLE);

        adapter.highlightItemAt(index - 1);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dimLayout.setVisibility(View.GONE);
            }
        });

        final Button closeButton =
                (Button) popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                popupWindow.dismiss();
            }
        });

    }
}

