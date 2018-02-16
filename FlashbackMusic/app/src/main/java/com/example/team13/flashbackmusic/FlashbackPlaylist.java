package com.example.team13.flashbackmusic;

import android.location.Location;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Eli on 2/12/2018.
 */


public class FlashbackPlaylist {

    final float METERS_IN_THOUSAND_FEET = 304.8f;

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

    /*
     * Constructs the Flashback Playlist from the user's current location, day, and time, and the
     * information stored in the song objects.
     */
    public FlashbackPlaylist(ArrayList<Song> allSongs, double[] location, String day, String time, String date) {

        playlist = new ArrayList<>();
        numMatches = new ArrayList<>();  // the number of matches the song has with the user's info

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
        sortPlaylist(date);
    }

    public static ArrayList<Song> sortPlaylist(String date){

        for(int i = LIKED_AND_THREE_MATCHES; i > 0; i--) {
            int index = numMatches.indexOf(i);
            ArrayList<Song> currentSongs = new ArrayList<>();

            while(index != -1) {
                currentSongs.add(playlist.get(index));
                playlist.remove(index);
                numMatches.remove(index);
                index = numMatches.indexOf(i);
            }
            if (currentSongs.isEmpty()){
                continue;
            }
            currentSongs = breakDateTies(currentSongs, date);
            playlist.addAll(currentSongs);
        }
        return playlist;
    }

    public static ArrayList<Song> breakTimeTies(ArrayList<Song> songs) {
        ArrayList<Integer> timeApart = new ArrayList<>();
        ArrayList<Song> result = new ArrayList<>();

        for(int i = 0; i < songs.size(); i++) {
            int num = compareTimes(songs.get(i).getLastTime());
            timeApart.add(num);
        }

        while(!timeApart.isEmpty()) {
            int minIndex = minIndex(timeApart);

            result.add(songs.get(minIndex));
            songs.remove(minIndex);
            timeApart.remove(minIndex);
        }

        return result;
    }

    public static int minIndex(ArrayList<Integer> list) {
        return list.indexOf (Collections.min(list));
    }


    public static ArrayList<Song> breakDateTies(ArrayList<Song> songs, String date) {
        ArrayList<Integer> daysApart = new ArrayList<>();
        ArrayList<Song> result = new ArrayList<>();

        for(int i = 0; i < songs.size(); i++) {
            int num = compareDates(songs.get(i).getLastDate(), date);
            daysApart.add(num);
        }

        ArrayList<Song> currentSongs = new ArrayList<>();
        int prev = daysApart.get(minIndex(daysApart));
        while(!daysApart.isEmpty()) {
            int minIndex = minIndex(daysApart);

            if(prev != daysApart.get(minIndex)) {
                result.addAll(breakTimeTies(currentSongs));
                currentSongs.clear();
                prev = daysApart.get(minIndex);
            }

            currentSongs.add(songs.get(minIndex));
            songs.remove(minIndex);
            daysApart.remove(minIndex);
        }

        result.addAll(breakTimeTies(currentSongs));

        return result;
    }

    public static int compareDates(String songDate, String thisDate) {
        int songMonth = Integer.parseInt(songDate.substring(0, songDate.indexOf(("/"))));
        String sub = songDate.substring(songDate.indexOf("/") + 1, songDate.length());
        int songDay = Integer.parseInt(sub.substring(0, sub.indexOf(("/"))));
        sub = sub.substring(sub.indexOf("/") + 1, sub.length());
        int songYear = Integer.parseInt(sub);

        int thisMonth = Integer.parseInt(thisDate.substring(0, thisDate.indexOf(("/"))));
        sub = thisDate.substring(thisDate.indexOf("/") + 1, thisDate.length());
        int thisDay = Integer.parseInt(sub.substring(0, sub.indexOf(("/"))));
        sub = sub.substring(sub.indexOf("/") + 1, sub.length());
        int thisYear = Integer.parseInt(sub);

        int diff = (thisYear - songYear) * 365;
        diff += (thisMonth - songMonth) * 30;
        diff += (thisDay - songDay);

        return diff;
    }

    public static int compareTimes(String songTime) {
        int songHour = Integer.parseInt(songTime.substring(0, songTime.indexOf(":")));
        int songMin = Integer.parseInt(songTime.substring(songTime.indexOf(":")+1, songTime.length()));

        int diff = (24 - songHour) * 60;
        diff -= songMin;

        return diff;
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
