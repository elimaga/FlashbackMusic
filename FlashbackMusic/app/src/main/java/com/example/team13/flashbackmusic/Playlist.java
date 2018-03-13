package com.example.team13.flashbackmusic;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Eli on 3/11/2018.
 */

public abstract class Playlist {

    final float METERS_IN_THOUSAND_FEET = 304.8f;

    // The variables that tell how many matches a song has with the user's current info and if
    // the song is liked to break ties. These are used to sort the playlist from highest to lowest
    final static int LIKED_AND_THREE_MATCHES = 6;
    final static int THREE_MATCHES = 5;
    final static int LIKED_AND_TWO_MATCHES = 4;
    final static int TWO_MATCHES = 3;
    final static int LIKED_AND_ONE_MATCH = 2;
    final static int ONE_MATCH = 1;
    final static int DISLIKED_SONG = 0;


    public static ArrayList<Song> playlist;      // the songs in the playlist
    public static ArrayList<Integer> numMatches;

    /**
     * Helper method for the constructor to check if the location of the song is close to the location
     * of the user.
     */
    public boolean matchesLocation(double curLatitude, double curLongitude, double prevLatitude, double prevLongitude) {

        // First check to make sure that each longitude and latitude is a valid coordinate
        if(curLatitude >= -90 && curLatitude <= 90 && curLongitude >= -180 && curLongitude <= 180 &&
                prevLatitude >= -90 && prevLatitude <= 90 && prevLongitude >= -180 && prevLongitude <= 180) {

            // Create location objects to measure the distance
            Location curLocation = new Location("");
            curLocation.setLatitude(curLatitude);
            curLocation.setLongitude(curLongitude);
            Location prevLocation = new Location("");
            prevLocation.setLatitude(prevLatitude);
            prevLocation.setLongitude(prevLongitude);

            if(curLocation.distanceTo(prevLocation) <= METERS_IN_THOUSAND_FEET) {
                return true;
            }
            else {
                return false;
            }

        }
        else {
            return false;
        }
    }

    public static int compareDates(String songDate, String curDate) {
        if(songDate.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        int[] songDateValues = UserInfo.getDateValues(songDate);
        int songMonth = songDateValues[0];
        int songDay = songDateValues[1];
        int songYear = songDateValues[2];

        int[] curDateValues = UserInfo.getDateValues(curDate);
        int curMonth = curDateValues[0];
        int curDay = curDateValues[1];
        int curYear = curDateValues[2];

        int diff = (curYear - songYear) * 365;
        diff += (curMonth - songMonth) * 30;
        diff += (curDay - songDay);

        return diff;
    }

    public static int compareTimes(String songTime) {
        if(songTime.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        int songHour = Integer.parseInt(songTime.substring(0, songTime.indexOf(":")));
        int songMin = Integer.parseInt(songTime.substring(songTime.indexOf(":")+1, songTime.length()));

        int diff = (24 - songHour) * 60;
        diff -= songMin;

        return diff;
    }

    public ArrayList<Song> getPlaylist() {
        return this.playlist;
    }

}
