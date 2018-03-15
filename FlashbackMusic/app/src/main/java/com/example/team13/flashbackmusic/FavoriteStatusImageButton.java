package com.example.team13.flashbackmusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by KM on 2/15/2018.
 */

public class FavoriteStatusImageButton extends android.support.v7.widget.AppCompatImageButton {

    private MusicLibrary musicLibrary;
    private Song song;

    public FavoriteStatusImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        musicLibrary = MusicLibrary.getInstance(context);
    }

    public void setSong(Song song){

        this.song = song;
        updateImage();
    }

    public void updateImage() {
        if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED){
            setImageResource(R.drawable.heart);
        }else if (song.getFavoriteStatus() == Song.FavoriteStatus.DISLIKED){
            setImageResource(R.drawable.dislike);
        }else{
            // song favoriteStatus is Neutral
            setImageResource(R.drawable.neutral);
        }
    }

    public void updateStatus(Context context) {

        String title = song.getTitle();

        if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED){
            song.setFavoriteStatus(Song.FavoriteStatus.DISLIKED);
            Log.d("Testing LIKED to DISLIKED","Symbol changes from LIKED to DISLIKED");

        }else if (song.getFavoriteStatus() == Song.FavoriteStatus.DISLIKED){
            song.setFavoriteStatus(Song.FavoriteStatus.NEUTRAL);
            Log.d("Testing DISLIKED to NEUTRAL","Symbol changes from DISLIKED to NEUTRAL");

        }else{
            // song favoriteStatus is Neutral
            song.setFavoriteStatus(Song.FavoriteStatus.LIKED);
            Log.d("Testing NEUTRAL to LIKED","Symbol changes from NEUTRAL to LIKED");
        }

        musicLibrary.persistSong(song);
    }

}
