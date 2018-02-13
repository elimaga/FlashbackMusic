package com.example.team13.flashbackmusic;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Eli on 2/12/2018.
 */


public class FlashbackPlaylist {

    final float METERS_IN_THOUSAND_FEET = 304.8f;

    // The variables that tell how many matches a song has with the user's current info and if
    // the song is liked to break ties. These are used to sort the playlist from highest to lowest
    final int LIKED_AND_THREE_MATCHES = 6;
    final int THREE_MATCHES = 5;
    final int LIKED_AND_TWO_MATCHES = 4;
    final int TWO_MATCHES = 3;
    final int LIKED_AND_ONE_MATCH = 2;
    final int ONE_MATCH = 1;


    private ArrayList<Song> playlist;      // the songs in the flashback playlist

    /*
     * Constructs the Flashback Playlist from the user's current location, day, and time, and the
     * information stored in the song objects.
     * TODO: Sort the playlist
     */
    public FlashbackPlaylist(ArrayList<Song> allSongs, double[] location, String day, String time) {

        ArrayList<Integer> numMatches = new ArrayList<>();  // the number of matches the song has with the user's info

        // Loop through every song in the array of songs and add the ones with at least one match
        // to the playlist
        for(Song song : allSongs) {

            // First check if it matches all 3 requirements
            if(matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                    && matchesDay(day, song.getLastDay()) && matchesTimeOfDay(time, song.getLastSetting())) {

                // Add the song to the playlist
                playlist.add(song);

                // If the song is liked, it should occur earlier in the playlist than a song that is
                // not liked
                if(song.isLiked()) {
                    numMatches.add(LIKED_AND_THREE_MATCHES);
                }
                else {
                    numMatches.add(THREE_MATCHES);
                }
            }
            // Else check if it matches 2 of the requirements
            else if((matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                    && matchesDay(day, song.getLastDay())) || (matchesLocation(location[0], location[1],
                    song.getLastLatitude(), song.getLastLongitude()) && matchesTimeOfDay(time, song.getLastSetting()))
                    || (matchesDay(day, song.getLastDay()) && matchesTimeOfDay(time, song.getLastSetting()))) {

                // Add the song to the playlist
                playlist.add(song);

                // If the song is liked, it should occur earlier in the playlist than a song that is
                // not liked
                if(song.isLiked()) {
                    numMatches.add(LIKED_AND_TWO_MATCHES);
                }
                else {
                    numMatches.add(TWO_MATCHES);
                }
            }
            // Else check if it matches 1 of the requirements
            else if(matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                    || matchesDay(day, song.getLastDay()) || matchesTimeOfDay(time, song.getLastSetting())) {

                // Add the song to the playlist
                playlist.add(song);

                // If the song is liked, it should occur earlier in the playlist than a song that is
                // not liked
                if (song.isLiked()) {
                    numMatches.add(LIKED_AND_ONE_MATCH);
                } else {
                    numMatches.add(ONE_MATCH);
                }
            }
        }

        sortPlaylist(numMatches);
    }

    private void sortPlaylist(ArrayList<Integer> numMatches) {
        ArrayList<Song> sorted = new ArrayList<>();

        for(int i = LIKED_AND_THREE_MATCHES; i > 0; i--) {
            int index = numMatches.indexOf(i);
            while(index != -1) {
                sorted.add(playlist.get(index));
                playlist.remove(index);
                numMatches.remove(index);
                index = numMatches.indexOf(i);
            }
        }
        playlist = sorted;
    }


    /*
     * Helper method for the constructor to check if the location of the song is close to the location
     * of the user.
     */
    private boolean matchesLocation(double curLatitude, double curLongitude, double prevLatitude, double prevLongitude) {

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

    /*
     * Helper method for the constructor to check if the current day of the user is the same as
     * the last time a song was played.
     */
    private boolean matchesDay(String curDay, String prevDay) {
        if(curDay.equals(prevDay)) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * Helper method for the constructor to check if the current time of day of the user is the same
     * as the last time a song was played.
     */
    private boolean matchesTimeOfDay(String curTime, String prevTimeOfDay) {

        // Get the current time of day of the user
        String curTimeOfDay = "";
        int hour = Integer.parseInt(curTime.substring(0, curTime.indexOf((":"))));

        if ((hour >= 5) && (hour < 11))
            curTimeOfDay = "Morning";
        else if ((hour >= 11) && (hour < 17))
            curTimeOfDay = "Afternoon";
        else
            curTimeOfDay = "Evening";



        // Then check to see if they match
        if(curTimeOfDay.equals(prevTimeOfDay)) {
            return true;
        }
        else {
            return false;
        }

    }


}
