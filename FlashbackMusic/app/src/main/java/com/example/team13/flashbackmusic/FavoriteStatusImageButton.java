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

    private Song song;

    public FavoriteStatusImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("flashback", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String title = song.getTitle();

        if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED){
            song.setFavoriteStatus(Song.FavoriteStatus.DISLIKED);

            String newStatus = Song.FavoriteStatus.DISLIKED.toString();
            editor.putString(title + "_favStatus", newStatus);

            Log.d("Testing LIKED to DISLIKED","Symbol changes from LIKED to DISLIKED");

        }else if (song.getFavoriteStatus() == Song.FavoriteStatus.DISLIKED){
            song.setFavoriteStatus(Song.FavoriteStatus.NEUTRAL);

            String newStatus = Song.FavoriteStatus.NEUTRAL.toString();
            editor.putString(title + "_favStatus", newStatus);

            Log.d("Testing DISLIKED to NEUTRAL","Symbol changes from DISLIKED to NEUTRAL");

        }else{
            // song favoriteStatus is Neutral
            song.setFavoriteStatus(Song.FavoriteStatus.LIKED);

            String newStatus = Song.FavoriteStatus.LIKED.toString();
            editor.putString(title + "_favStatus", newStatus);

            Log.d("Testing NEUTRAL to LIKED","Symbol changes from NEUTRAL to LIKED");
        }

        editor.apply();
    }

}
