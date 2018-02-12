package com.example.team13.flashbackmusic;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("SONG"));
        tabLayout.addTab(tabLayout.newTab().setText("ALBUM"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter( getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
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

        final Context context = getApplicationContext();
        ImageButton flashBackButton = findViewById(R.id.flashback_button);
        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: transition flashback mode activity!!
                // TODO: Please remove the following line
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                playSong();
            }
        });
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
