package com.example.team13.flashbackmusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;

public class PlayService extends Service {

    public PlayService() {
    }


    /**
     * Thread for the songs to play on
     */
    final class MyThread implements Runnable {
        int startId;
        ArrayList<Integer> resourceIds;
        Resources resources;

        public MyThread(int startId, ArrayList<Integer> resourceIds, Resources resources) {
            this.startId = startId;
            this.resourceIds = resourceIds;
            this.resources = resources;
        }

        @Override
        public void run() {
            synchronized (this) {
                SongPlayer songPlayer = new SongPlayer(resourceIds, resources);
                songPlayer.playMusic();

                stopSelf(startId);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        ArrayList<Integer> resourceIds = extras.getIntegerArrayList("resourceIds");
        Thread thread = new Thread(new MyThread(startId, resourceIds, this.getResources()));
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
