package com.example.team13.flashbackmusic;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.MimeTypeFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team13.flashbackmusic.interfaces.DownloadObserver;
import com.example.team13.flashbackmusic.interfaces.MusicLibraryObserver;
import com.example.team13.flashbackmusic.interfaces.UnzipperObserver;

import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements UnzipperObserver, DownloadObserver, MusicLibraryObserver {

    MusicFileDownloader musicFileDownloader;
    Unzipper unzipper;
    Button downloadButton;
    TextView textView;
    EditText urlEditText;
    private boolean isAidle;
    AlertDialog.Builder builder;
    MusicLibrary musicLibrary;
    String urlString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        downloadButton = findViewById(R.id.downloadButton);
        textView = findViewById(R.id.textView);
        urlEditText = findViewById(R.id.editText);
        musicFileDownloader = new MusicFileDownloader(DownloadActivity.this);
        unzipper = new Unzipper();
        unzipper.registerObserver(this);
        isAidle = true;
        musicLibrary = MusicLibrary.getInstance(DownloadActivity.this);
        musicLibrary.registerObserver(this);


        builder = new AlertDialog.Builder(DownloadActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Exit download")
                .setMessage("Songs have not been added to library. Do you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // exit
                        DownloadActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);



        if (!hasStoragePermission()) {

            //permission not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public boolean hasStoragePermission() {
        return ContextCompat.checkSelfPermission(DownloadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(DownloadActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }

    public void downloadPressed(View view) {
        urlString = urlEditText.getText().toString();

        if (hasStoragePermission()) {
            if (URLUtil.isValidUrl(urlString)) {

                URL url;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                }

                musicFileDownloader.registerReceiver(this);
                musicFileDownloader.downloadMusicFile(url);

                //disable the button
                downloadButton.setEnabled(false);
                textView.setText("Downloading...");
                isAidle = false;
            } else {
                Toast.makeText(DownloadActivity.this, "URL is not valid", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(DownloadActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (isAidle) {
            super.onBackPressed();
        } else {
            builder.show();
        }
    }

    public void close(View view) {
        onBackPressed();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            musicFileDownloader.unregisterReceiver(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        unzipper.removeObserver(this);
        musicLibrary.removeObserver(this);
    }

    // DownloadObserver
    @Override
    public void onCompleteDownload(Context context, Intent intent) {
        String mime = intent.getStringExtra("mime");
        String filename = intent.getStringExtra("filename");
        String directoryPath = intent.getStringExtra("directoryPath");
        if (MimeTypeFilter.matches(mime, new String[]{"audio/mpeg","audio/mpeg3","audio/x-mpeg-3","video/mpeg","video/x-mpeg"})!= null){
            ArrayList<String> argArrayList = new ArrayList<>();
            argArrayList.add(directoryPath+filename);
            argArrayList.add(urlString);
            musicLibrary.execute(argArrayList.toArray(new String[0]));
            reset();
        } else if (MimeTypeFilter.matches(mime,
                new String[]{"application/x-compressed", "application/x-zip-compressed","application/zip","multipart/x-zip"})!= null) {

            textView.setText("Unzipping...");
            unzipper.execute(directoryPath, filename);

        } else {
            Toast.makeText(context, "invalid file format", Toast.LENGTH_SHORT).show();
            downloadButton.setEnabled(true);
            reset();
        }
    }

    // Unzipper Observer
    @Override
    public void onUnzipSuccess(ArrayList<String> paths) {
        textView.setText("Loading files into library...");
        // calling addSongsIntoLibraryFromPath internally
        String url = urlEditText.getText().toString();
        for (int i = 0; i < paths.size(); i++) {
            try {
                String extension = MimeTypeMap.getFileExtensionFromUrl(new File(paths.get(i)).toURI().toURL().toString());
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String mime = mimeTypeMap.getMimeTypeFromExtension(extension);

                if (MimeTypeFilter.matches(mime, new String[]{"audio/mpeg",
                        "audio/mpeg3",
                        "audio/x-mpeg-3",
                        "video/mpeg",
                        "video/x-mpeg"}) == null) {
                    paths.remove(paths.get(i));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        musicLibrary.updateLibraryInBackground(paths, url);
    }

    @Override
    public void onUnzipFailure() {
        downloadButton.setEnabled(true);
        Log.d("unzip", "unzipping failed");
        reset();
    }

    @Override
    public void onCompleteUpdate() {
        textView.setText("Library got updated!");
        reset();
    }

    void reset() {
        isAidle = true;
        textView.setText("");
        downloadButton.setEnabled(true);
        urlString = "";
    }
}
