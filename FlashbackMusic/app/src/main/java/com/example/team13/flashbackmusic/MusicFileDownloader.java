package com.example.team13.flashbackmusic;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.team13.flashbackmusic.interfaces.MusicFileDownloaderObserver;
import com.example.team13.flashbackmusic.interfaces.Subject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Kazutaka on 3/3/18.
 */

public class MusicFileDownloader {

    ArrayList<MusicFileDownloaderObserver> observers;

    private Context mContext;
    private DownloadManager downloadManager;
    private ArrayList<MusicFileDownloaderObserver> musicFileManagerObservers;
    private BroadcastReceiver broadcastReceiver;

    public MusicFileDownloader(Context context) {
        this.downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        this.mContext = context;
    }

    public long downloadMusicFile (URL url) {

        Uri uri = null;
        try{
            uri = Uri.parse(url.toURI().toString());
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        long downloadReference;

        // Create request for android download manager
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting description of request
        request.setDescription("Downloading a music file");


        //String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        //Log.d("dir",dir);
        //request.setDestinationInExternalPublicDir( dir, URLUtil.guessFileName(url.toString(),null,null));
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_MUSIC, URLUtil.guessFileName(url.toString(),null,null));

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(broadcastReceiver, filter);
    }
    public void unRegisterReceiver(BroadcastReceiver broadcastReceiver){
        mContext.unregisterReceiver(broadcastReceiver);
    }
}
