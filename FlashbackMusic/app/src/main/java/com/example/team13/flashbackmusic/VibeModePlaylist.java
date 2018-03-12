package com.example.team13.flashbackmusic;

import java.util.ArrayList;

/**
 * Created by Eli on 3/11/2018.
 */

public class VibeModePlaylist {

    // The variables that tell how many matches a song has with the user's current info and if
    // the song is liked to break ties. These are used to sort the playlist from highest to lowest
    final static int LIKED_AND_THREE_MATCHES = 6;
    final static int THREE_MATCHES = 5;
    final static int LIKED_AND_TWO_MATCHES = 4;
    final static int TWO_MATCHES = 3;
    final static int LIKED_AND_ONE_MATCH = 2;
    final static int ONE_MATCH = 1;


    public static ArrayList<Song> playlist;      // the songs in the flashback playlist
    public static ArrayList<Integer> numMatches;
}
