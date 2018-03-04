package com.example.team13.flashbackmusic;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;

import com.example.team13.flashbackmusic.interfaces.MusicFileDownloaderObserver;
import com.example.team13.flashbackmusic.interfaces.Subject;

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
        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
    }

    private long DownloadMusicFile (Uri uri) {

        long downloadReference;

        // Create request for android download manager
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting description of request
        request.setDescription("Downloading a music file");

        request.setDestinationInExternalFilesDir(mContext, null,"newFile");

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(broadcastReceiver, filter);
    }
}
