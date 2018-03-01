package com.example.team13.flashbackmusic;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Observer;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Kazutaka on 2/28/18.
 */

public class MusicFileManager implements com.example.team13.flashbackmusic.Subject {

    private Context mContext;
    private DownloadManager downloadManager;
    private ArrayList<MusicFileManagerObserver> musicFileManagerObservers;

    public MusicFileManager(Context context) {
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

    @Override
    public void registerObserver(MusicFileManagerObserver observer) {
        musicFileManagerObservers.add(observer);
    }
    

    @Override
    public void removeObserver(MusicFileManagerObserver observer) {
        int i = musicFileManagerObservers.indexOf(observer);
        if (i > 0) {
            musicFileManagerObservers.remove(i);
        }
    }

    @Override
    public void notifyObservers() {
        for (MusicFileManagerObserver observer : musicFileManagerObservers) {
            observer.onCompleteDownloadAndUnzip();
        }
    }
}
