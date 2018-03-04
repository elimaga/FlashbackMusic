package com.example.team13.flashbackmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class DownloadActivity extends AppCompatActivity {

    MusicFileDownloader musicFileDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        musicFileDownloader = new MusicFileDownloader(DownloadActivity.this);
    }

    protected void close() {
        finish();
    }

    protected void downloadPressed() {
        musicFileDownloader.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "finished downloading", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
