package com.example.team13.flashbackmusic;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        playlist = new ArrayList<>();
        numMatches = new ArrayList<>();
    }

    public boolean addSong(Song song) {
        int reqMatches = numMatchesOfSong(song);

        // If the song is disliked, we don't want to add it to the vibeModePlaylist
        if(reqMatches == DISLIKED_SONG) {
            return false;
        }

        // Check if the song is already in the vibeModePlaylist so we don't have repeats
        int playlistIndex = playlist.indexOf(song);
        // If the song is already in the vibeModePlaylist, check which song (the previous or the current) has
        // more requirement matches
        if(playlistIndex != -1) {
            // If the number of requirement matches for the current instance of the song is greater
            // than the number of matches for the song that is already in the vibeModePlaylist, then replace
            // the previous song with the current one
            if(reqMatches > numMatches.get(playlistIndex)) {
                playlist.set(playlistIndex, song);
                numMatches.set(playlistIndex, reqMatches);
                return true;
            }
            // Else if the number of matches is the same, check which song has the higher priority
            else if(reqMatches == numMatches.get(playlistIndex)) {
                // If the song that is not in the VibeModePlaylist has a higher priority, put it in the VibeModePlaylist,
                // replacing the old instance of the song
                if(hasHigherPriority(song, playlist.get(playlistIndex))) {
                    playlist.set(playlistIndex, song);
                    numMatches.set(playlistIndex, reqMatches);
                    return true;
                }
            }
            return false;
        }
        // Else the song isn't in the vibeModePlaylist yet, so add it
        else {
            playlist.add(song);
            numMatches.add(reqMatches);
            return true;
        }
    }

    /**
     * Helper method to see how many matches the song has with the requirements for Vibe Mode
     * @param song - the song to get the number of matches for
     * @return - the Vibe Mode requirements the song satisfies
     */
    public int numMatchesOfSong(Song song) {
        // First check if it matches all 3 requirements
        if(matchesLocation(location[0], location[1], song.getLastLatitude(), song.getLastLongitude())
                && matchesDate(song.getLastDate()) && matchesFriend(song.getLastUser())) {

            // Only add the songs that are not disliked
            if (song.getFavoriteStatus() != Song.FavoriteStatus.DISLIKED) {

                // If the song is liked, it should occur earlier in the vibeModePlaylist than a song that is
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

                // If the song is liked, it should occur earlier in the vibeModePlaylist than a song that is
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

                // If the song is liked, it should occur earlier in the vibeModePlaylist than a song that is
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
     * their date and time if the songs have the same matches.
     * @param newSong - the song that is not in the playlist
     * @param oldSong - the song that has already been added to the playlist
     * @return - returns true if the new song has higher priority, false otherwise
     */
    public boolean hasHigherPriority(Song newSong, Song oldSong) {
        // If both are within 1000 feet of the user location, check the dates
        if(matchesLocation(location[0], location[1], newSong.getLastLatitude(), newSong.getLastLongitude())
                == matchesLocation(location[0], location[1], oldSong.getLastLatitude(), oldSong.getLastLongitude()))
        {
            // If both songs have been played within a week of today, check if they were played by a friend
            if(matchesDate(newSong.getLastDate()) == matchesDate(oldSong.getLastDate())) {
                // If both songs were played by a friend, check which song was more recently played
                if (matchesFriend(newSong.getLastUser()) == matchesFriend(oldSong.getLastUser())) {

                    // Check how many days apart the songs were played
                    int daysDiff = compareDates(newSong.getLastDate(), oldSong.getLastDate());

                    // If they were played on the same day, check the time they were played
                    if(daysDiff == 0) {

                        // If the time for the new song is later than the time for the old song, then
                        // return true since the new song is newer
                        int newTimeDiff = compareTimes(newSong.getLastTime());
                        int oldTimeDiff = compareTimes(oldSong.getLastTime());
                        if(newTimeDiff < oldTimeDiff) {
                            return true;
                        }
                        // Else, return false because the old song is newer or was played at the same time
                        else {
                            return false;
                        }
                    }
                    // Else if the new song was played on a later date, return true
                    else if(daysDiff < 0) {
                        return true;
                    }
                    // Else, return false because the old song was played on a later date
                    else {
                        return false;
                    }
                }
                // Else if the new song was played by a friend, then return true because the old song
                // was not played by a friend
                else if(matchesFriend(newSong.getLastUser())) {
                    return true;
                }
                // Else the new song was not played by a friend, but the old song was, so return false
                else {
                    return false;
                }
            }
            // Else if the new song was played within the last week, then return true because the old
            // song wasn't played within the last week
            else if(matchesDate(newSong.getLastDate())) {
                return true;
            }
            // Else the new song wasn't played within the last week, but the old song was, so return false
            else {
                return false;
            }
        }
        // Else if the new song matches the user's location, then return true because the old song is not
        // within 1000 feet of the user's location
        else if(matchesLocation(location[0], location[1], newSong.getLastLatitude(), newSong.getLastLongitude()))
        {
            return true;
        }
        // Else, return false because the new song was not played within 1000 feet of the user's location,
        // but the old song was
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

    public void sortPlaylist(String date){
        for(int i = LIKED_AND_THREE_MATCHES; i > 0; i--) {
            int index = numMatches.indexOf(i);
            ArrayList<Song> currentSongs = new ArrayList<>();
            ArrayList<Integer> currentMatches = new ArrayList<>();

            while(index != -1) {
                currentSongs.add(playlist.get(index));
                playlist.remove(index);
                currentMatches.add(numMatches.remove(index));
                index = numMatches.indexOf(i);
            }
            if (currentSongs.isEmpty()){
                continue;
            }
            breakRequirementTies(currentSongs);
            playlist.addAll(currentSongs);
            numMatches.addAll(currentMatches);
        }
    }

    public void sortPlaylist() {
        sortPlaylist(this.date);
    }

    public void breakRequirementTies(ArrayList<Song> songs) {
        for(int index = 1; index < songs.size(); index++) {
            Song currSong = songs.get(index);
            int secondIndex = index - 1;
            Song tempSong = songs.get(secondIndex);
            while(secondIndex >= 0 && hasHigherPriority(currSong, tempSong)) {
                songs.set(secondIndex + 1, tempSong);
                secondIndex--;
                if(secondIndex >= 0) {
                    tempSong = songs.get(secondIndex);
                }
                Log.d("Sorting: ", "Moving " + currSong.getTitle() + " in front of " + tempSong.getTitle());
            }
            songs.set(secondIndex + 1, currSong);
        }
    }

}
