package com.example.team13.flashbackmusic;

import java.time.Duration;

/**
 * Created by Kazutaka on 2/9/18.
 */

public class Song {
    String title;
    String artist;
    String duration;

    public Song(String title, String artist, String duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }
}