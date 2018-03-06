package com.example.team13.flashbackmusic;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.MimeTypeFilter;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.example.team13.flashbackmusic.interfaces.DownloadObserver;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * Created by Kazutaka on 3/3/18.
 */

public class MusicFileDownloader {

    DownloadObserver observer;
    Long downloadReference;

    private Context mContext;
    private DownloadManager downloadManager;
    private ArrayList<DownloadObserver> musicFileManagerObservers;
    private BroadcastReceiver broadcastReceiver;
    private String filename;
    private String directoryPath;

    public MusicFileDownloader(Context context) {
        this.downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        this.mContext = context;
    }

    public void downloadMusicFile (URL url) {

        Uri uri = null;
        try{
            uri = Uri.parse(url.toURI().toString());
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        // Create request for android download manager
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting description of request
        request.setDescription("Downloading a music file");
        String mimeType = MimeTypeMap.getFileExtensionFromUrl(url.toString());
        filename = URLUtil.guessFileName(url.toString(), null, mimeType);
        directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, filename);

        final long dr = downloadManager.enqueue(request);

        downloadReference = dr;

    }

    public void registerReceiver(DownloadObserver observer) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onComplete();
            }
        };
        mContext.registerReceiver(broadcastReceiver, filter);

        this.observer = observer;
    }
    public void unregisterReceiver(DownloadObserver observer){
        mContext.unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
        if(this.observer.equals(observer)) observer = null;
    }

    public void onComplete() {
        String mime = downloadManager.getMimeTypeForDownloadedFile(downloadReference);
        Intent intent = new Intent();
        intent.putExtra("filename", filename);
        intent.putExtra("directoryPath", directoryPath);
        intent.putExtra("mime", mime);
        observer.onCompleteDownload(mContext, intent);

    }
}
