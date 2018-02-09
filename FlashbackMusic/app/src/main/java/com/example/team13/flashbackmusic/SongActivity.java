package com.example.team13.flashbackmusic;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class SongActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        TextView songNameView = (TextView) findViewById(R.id.nameTextView);
        TextView songArtistView = (TextView) findViewById(R.id.artistTextView);
        TextView songAlbumView = (TextView) findViewById(R.id.albumTextView);
        TextView songLocationView = (TextView) findViewById(R.id.locationTextView);
        TextView songDayView = (TextView) findViewById(R.id.dayTextView);
        TextView songTimeView = (TextView) findViewById(R.id.timeTextView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String path = "";

        if(extras != null)
        {
            /*
            songNameView.setText("Name: " + (String) extras.getString("name"));
            songArtistView.setText("Artist: " + (String) extras.getString("artist"));
            songAlbumView.setText("Album: " + (String) extras.getString("album"));
            songLocationView.setText("Location: " + (String) extras.getString("location"));
            songDayView.setText("Day: " + (String) extras.getString("day"));
            songTimeView.setText("Time: " + (String) extras.getString("time"));
            */

            path = (String) extras.getString("path");
        }

        //get new location, day, and time
        String newDay = getDay();
        String newTime = getTime();

        System.out.println(newDay + " " + newTime);
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

    public String getDay()
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

    public String getTime()
    {
        Date currentTime = Calendar.getInstance().getTime();
        int hours = currentTime.getHours();
        int mins = currentTime.getMinutes();
        String ampm;

        if (hours == 0)
        {
            hours = hours + 12;
            ampm = "am";
        }
        else if(hours < 11)
        {
            ampm = "am";
        }
        else if(hours > 12)
        {
            hours = hours - 12;
            ampm = "pm";
        }
        else
        {
            ampm = "pm";
        }

        if(mins < 10)
        {
            return hours + ":0" + mins + " " + ampm;
        }
        else
        {
            return hours + ":" + mins + " " + ampm;
        }
    }
}
