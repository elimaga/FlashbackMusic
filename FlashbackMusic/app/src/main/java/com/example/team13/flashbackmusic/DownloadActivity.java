package com.example.team13.flashbackmusic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team13.flashbackmusic.interfaces.DownloadObserver;

import java.net.MalformedURLException;
import java.net.URL;

public class DownloadActivity extends AppCompatActivity {

    MusicFileDownloader musicFileDownloader;
    long downloadReference;
    Unzipper unzipper;
    Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        downloadButton = findViewById(R.id.downloadButton);

        musicFileDownloader = new MusicFileDownloader(DownloadActivity.this);
        unzipper = new Unzipper();

        if(ContextCompat.checkSelfPermission(DownloadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(DownloadActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){

            //permission not granted
            ActivityCompat.requestPermissions ( this ,
                    new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1 );
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    public void close(View view) {
        finish();
    }

    public void downloadPressed(View view) {
        EditText editText = findViewById(R.id.editText);
        String urlString = editText.getText().toString();


        if (URLUtil.isValidUrl(urlString)){

            URL url;
            try{
                url = new URL(urlString);
            }catch(MalformedURLException e){
                e.printStackTrace();
                return;
            }

            musicFileDownloader.registerReceiver(unzipper);
            musicFileDownloader.downloadMusicFile(url);

            //disable the button
            downloadButton.setEnabled(false);

        }
        else{
            Toast.makeText(DownloadActivity.this,"URL is not valid", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        musicFileDownloader.unregisterReceiver(unzipper);
    }
}
