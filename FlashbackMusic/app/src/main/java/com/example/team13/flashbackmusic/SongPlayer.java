package com.example.team13.flashbackmusic;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Eli on 2/15/2018.
 */

public class SongPlayer {

    private MediaPlayer mediaPlayer;
    ArrayList<Integer> resIds;
    Resources resources;
    int index;

    public SongPlayer(ArrayList<Integer> resIds, Resources resources) {
        this.resIds = resIds;
        mediaPlayer = new MediaPlayer();
        this.resources = resources;
    }

    public void playMusic() {

        index = 0;  // index for which song we are playing
        loadMedia(resIds.get(index));

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                if (mediaPlayer.isPlaying())
                {
                    Log.d("Testing Playback", "Song is playing");
                    index++;
                }
                else
                {
                    Log.d("Testing Playback", "Song is playing");
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(index < resIds.size()) {
                    mediaPlayer.reset();
                    loadMedia(resIds.get(index));
                }
            }
        });

    }

    private void loadMedia(int resId)
    {
        try {
            AssetFileDescriptor afd = resources.openRawResourceFd(resId);
            mediaPlayer.setDataSource(afd);
            mediaPlayer.prepareAsync();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onDestroy() {
        mediaPlayer.release();
    }


}
