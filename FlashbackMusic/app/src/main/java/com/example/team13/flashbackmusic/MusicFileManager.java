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
 * Created by Kazutaka on 2/28/18.
 */

public class MusicFileManager {

    private Context mContext;
    private ArrayList<MusicFileDownloaderObserver> musicFileManagerObservers;

    public MusicFileManager(Context context) {
    }

}
