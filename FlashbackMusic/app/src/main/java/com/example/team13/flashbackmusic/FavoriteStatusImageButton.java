package com.example.team13.flashbackmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by KM on 2/15/2018.
 */

public class FavoriteStatusImageButton extends android.support.v7.widget.AppCompatImageButton
                                        implements View.OnClickListener {

    private Song song;

    public FavoriteStatusImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick (View view){
        updateStatus();
        updateImage();
    }
    public void setSong(Song song){
        this.song = song;
        updateImage();
    }

    private void updateImage() {
        if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED){
            setImageResource(R.drawable.heart);
        }else if (song.getFavoriteStatus() == Song.FavoriteStatus.DISLIKED){
            setImageResource(R.drawable.dislike);
        }else{
            // song favoriteStatus is Neutral
            setImageResource(R.drawable.neutral);
        }
    }

    private void updateStatus() {
        if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED){
            song.setFavoriteStatus(Song.FavoriteStatus.DISLIKED);

        }else if (song.getFavoriteStatus() == Song.FavoriteStatus.DISLIKED){
            song.setFavoriteStatus(Song.FavoriteStatus.NEUTRAL);
        }else{
            // song favoriteStatus is Neutral
            song.setFavoriteStatus(Song.FavoriteStatus.LIKED);
        }
    }

}
