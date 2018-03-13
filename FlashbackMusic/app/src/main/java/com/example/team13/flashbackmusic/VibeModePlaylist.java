package com.example.team13.flashbackmusic;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Eli on 3/11/2018.
 */

public class VibeModePlaylist extends Playlist {

    final int DAYS_IN_WEEK = 7;

    double[] location;
    String date;
    ArrayList<String> friends;

    public VibeModePlaylist(double[] location, String date, ArrayList<String> friends) {
        this.location = location;
        this.date = date;
        this.friends = friends;
    }

    public void addSong(Song song) {
        int reqMatches = numMatchesOfSong(song);

        // If the song is disliked, we don't want to add it to the playlist
        if(reqMatches == DISLIKED_SONG) {
            return;
        }

        // Check if the song is already in the playlist so we don't have repeats
        int index = playlist.indexOf(song);
        // If the song is already in the playlist, check which song (the previous or the current) has
        // more requirement matches
        if(index != -1) {
            // If the number of requirement matches for the current instance of the song is greater
            // than the number of matches for the song that is already in the playlist, then replace
            // the previous song with the current one
            if(reqMatches > numMatches.get(index)) {
                playlist.set(index, song);
                numMatches.set(index, reqMatches);
            }
            else if(reqMatches == numMatches.get(index)) {

            }
        }
        // Else the song isn't in the playlist yet, so add it
        else {
            playlist.add(song);
            numMatches.add(reqMatches);
        }
    }

    public int numMatchesOfSong(Song song) {
        // First check if it matches all 3 requirements
        if(matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                && matchesDate(song.getLastDate()) && matchesFriend(song.getLastUser())) {

            // Only add the songs that are not disliked
            if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {

                // If the song is liked, it should occur earlier in the playlist than a song that is
                // not liked
                if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                    return LIKED_AND_THREE_MATCHES;
                }
                else {
                    return THREE_MATCHES;
                }
            }
            else {
                Log.d("Disliked Song", "Skipping");
                return DISLIKED_SONG;
            }

        }
        // Else check if it matches 2 of the requirements
        else if((matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                && matchesDate(song.getLastDate())) || (matchesLocation(location[0], location[1],
                song.getLastLatitude(), song.getLastLongitude()) && matchesFriend(song.getLastUser()))
                || (matchesDate(song.getLastDate()) && matchesFriend(song.getLastUser()))) {

            // Only add the songs that are not disliked
            if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {

                // If the song is liked, it should occur earlier in the playlist than a song that is
                // not liked
                if (song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                    return LIKED_AND_TWO_MATCHES;
                } else {
                    return TWO_MATCHES;
                }
            }
            else {
                Log.d("Disliked Song", "Skipping");
                return DISLIKED_SONG;
            }

        }
        // Else it matched 1 of the requirements
        else {

            // Only add the songs that are not disliked
            if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {

                // If the song is liked, it should occur earlier in the playlist than a song that is
                // not liked
                if (song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                    return LIKED_AND_ONE_MATCH;
                } else {
                    return ONE_MATCH;
                }
            }
            else {
                Log.d("Disliked Song", "Skipping");
                return DISLIKED_SONG;
            }

        }
    }

    /**
     * Breaks ties between duplicate songs. First checks if both song match requirements (a)-(c), then checks
     * there date and time if the songs have the same matches.
     * @param newSong - the song that is not in the playlist
     * @param oldSong - the song that has already been added to the playlist
     * @return - returns true if the new song has higher priority, false otherwise
     */
    public boolean hasHigherPriority(Song newSong, Song oldSong) {
        if(matchesLocation(location[0], location[1], newSong.getLastLatitude(), newSong.getLastLongitude())
                == matchesLocation(location[0], location[1], oldSong.getLastLatitude(), oldSong.getLastLongitude()))
        {
            if(matchesDate(newSong.getLastDate()) == matchesDate(oldSong.getLastDate())) {
                if (matchesFriend(newSong.getLastUser()) == matchesFriend(oldSong.getLastUser())) {
                    int daysDiff = compareDates(newSong.getLastDate(), oldSong.getLastDate());
                    if(daysDiff == 0) {
                        int newTimeDiff = compareTimes(newSong.getLastTime());
                        int oldTimeDiff = compareTimes(oldSong.getLastTime());
                        if(newTimeDiff < oldTimeDiff) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else if(daysDiff < 0) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else if(matchesFriend(newSong.getLastUser())) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else if(matchesDate(newSong.getLastDate())) {
                return true;
            }
            else {
                return false;
            }
        }
        else if(matchesLocation(location[0], location[1], newSong.getLastLatitude(), newSong.getLastLongitude()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Helper method to determine if the song was last played within a week of the date today
     * @param lastDate - the last date the song was played on
     * @return - true if the song was played within a week of today, false if not
     */
    public boolean matchesDate(String lastDate) {
        int[] curDateValues = UserInfo.getDateValues(date);
        int month = curDateValues[0];
        int day = curDateValues[1];
        int year = curDateValues[2];

        // Loop 7 times through the days in the past week
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            String dateToCheck = month + "/" + day + "/" + year;

            // If the last date the song was played on is within a week of the current date, then
            // return true
            if(dateToCheck.equals(lastDate)) {
                return true;
            }

            // Rolls the date back one day
            int[] newDateValues = UserInfo.backOneDay(month, day, year);
            month = newDateValues[0];
            day = newDateValues[1];
            year = newDateValues[2];
        }

        // Return false if the song was not played within the last week
        return false;
    }

    /**
     * Helper method to determine if the last user to play the song was a friend
     * @param lastUser - the last user who played the song
     * @return - true if the last user is a friend, false if not
     */
    public boolean matchesFriend(String lastUser) {
        return friends.contains(lastUser);
    }

}
