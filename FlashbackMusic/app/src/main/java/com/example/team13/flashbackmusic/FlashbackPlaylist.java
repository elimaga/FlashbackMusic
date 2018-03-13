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


public class FlashbackPlaylist extends Playlist {

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

                // Only add the songs that are not disliked
                if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {
                    playlist.add(song);

                    // If the song is liked, it should occur earlier in the playlist than a song that is
                    // not liked
                    if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                        numMatches.add(LIKED_AND_THREE_MATCHES);
                    }
                    else {
                        numMatches.add(THREE_MATCHES);
                    }
                }
                else {
                    Log.d("Disliked Song", "Skipping");
                }

            }
            // Else check if it matches 2 of the requirements
            else if((matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                    && matchesDay(day, song.getLastDay())) || (matchesLocation(location[0], location[1],
                    song.getLastLatitude(), song.getLastLongitude()) && matchesTimeOfDay(time, song.getLastSetting()))
                    || (matchesDay(day, song.getLastDay()) && matchesTimeOfDay(time, song.getLastSetting()))) {

                // Only add the songs that are not disliked
                if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {
                    playlist.add(song);

                    // If the song is liked, it should occur earlier in the playlist than a song that is
                    // not liked
                    if (song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                        numMatches.add(LIKED_AND_TWO_MATCHES);
                    } else {
                        numMatches.add(TWO_MATCHES);
                    }
                }
                else {
                    Log.d("Disliked Song", "Skipping");
                }

            }
            // Else check if it matches 1 of the requirements
            else if(matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                    || matchesDay(day, song.getLastDay()) || matchesTimeOfDay(time, song.getLastSetting())) {

                // Only add the songs that are not disliked
                if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {
                    playlist.add(song);


                    // If the song is liked, it should occur earlier in the playlist than a song that is
                    // not liked
                    if (song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                        numMatches.add(LIKED_AND_ONE_MATCH);
                    } else {
                        numMatches.add(ONE_MATCH);
                    }
                }
                else {
                    Log.d("Disliked Song", "Skipping");
                }

            }
        }

        // If the playlist is empty, then all the recently played songs should be added to the playlist
        if(playlist.isEmpty()) {
            for(Song song : allSongs) {
                // If the song has been played at all, then it will have a lastDate played so add it
                // to the playlist
                if(song.getLastDate() != "") {
                    // Only add the songs that are not disliked
                    if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {
                        playlist.add(song);

                        // If the song is liked, it should occur earlier in the playlist than a song that is
                        // not liked
                        if (song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                            numMatches.add(LIKED_AND_ONE_MATCH);
                        } else {
                            numMatches.add(ONE_MATCH);
                        }
                    }
                    else {
                        Log.d("Disliked Song", "Skipping");
                    }
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


    /*
     * Helper method for the constructor to check if the current day of the user is the same as
     * the last time a song was played.
     */
    public boolean matchesDay(String curDay, String prevDay) {
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
    public boolean matchesTimeOfDay(String curTime, String prevTimeOfDay) {

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
