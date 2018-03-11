package com.example.team13.flashbackmusic;

import android.util.Log;

import com.example.team13.flashbackmusic.interfaces.SongObserver;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class DatabaseMediator implements SongObserver {

    final double KILOMETERS_IN_THOUSAND_FEET = 0.3048;
    final int DAYS_IN_WEEK = 7;
    Song song;
    ArrayList<String> queriedSongs;

    public DatabaseMediator(Song song)
    {
        this.song = song;
        song.registerObserver(this);
        queriedSongs = new ArrayList<>();
    }

    /**
     * Method that gets called when the data changes in the song object. Delegates to the send
     * method.
     */
    public void update() {
        send();
    }

    /**
     * Sends Song/Location data to Google Firebase database
     */
    private void send()
    {
        String username = "usr1";
        String databaseKey = username + "_" + song.getTitle() + "_" + song.getArtist();

        DatabaseEntry databaseEntry = new DatabaseEntry();
        databaseEntry.setTitle(song.getTitle());
        databaseEntry.setArtist(song.getArtist());
        databaseEntry.setAlbumName(song.getAlbumName());
        databaseEntry.setLastDay(song.getLastDay());
        databaseEntry.setLastTime(song.getLastTime());
        databaseEntry.setLastDate(song.getLastDate());
        databaseEntry.setLastLatitude(song.getLastLatitude());
        databaseEntry.setLastLongitude(song.getLastLongitude());
        databaseEntry.setURL(song.getUrl());
        databaseEntry.setTrackNumber(song.getTrack());
        databaseEntry.setUsername(username);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference songReference = firebaseDatabase.getReference("Songs");
        DatabaseReference locationReference = firebaseDatabase.getReference("Locations");
        GeoFire geoFire = new GeoFire(locationReference);

        songReference.child(databaseKey).setValue(databaseEntry, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                Log.d("Send Song Data","Values were set for song: " + song.getTitle());
            }
        });

        geoFire.setLocation(databaseKey + "-location", new GeoLocation(song.getLastLatitude(), song.getLastLongitude()),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            Log.d("Send GeoLocation", "There was an error saving the location to GeoFire: " + error);
                        } else {
                            Log.d("Send GeoLocation","Location saved on server successfully!");
                        }
                    }
                });
    }

    /**
     * Method to retrieve all songs that are within 1000 feet of the user's location
     * @param latitude - the user's current latitude
     * @param longitude - the user's current latitude
     */
    public void retrieveSongsByLocation(double latitude, double longitude) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference locationReference = firebaseDatabase.getReference("Locations");
        GeoFire geoFire = new GeoFire(locationReference);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), KILOMETERS_IN_THOUSAND_FEET);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d("Retrieve Songs by Location", String.format("Key %s is within 1000 feet at [%f,%f]", key, location.latitude, location.longitude));

                // Add the songKey to the ArrayList of queried songs
                String songKey = key.substring(0, key.indexOf("-"));
                queriedSongs.add(songKey);
            }

            @Override
            public void onKeyExited(String key) {
                Log.d("Retrieve Songs by Location", String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("Retrieve Songs by Location", String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                Log.d("Retrieve Songs by Location", "All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d("Retrieve Songs by Location", "Error: " + error);
            }
        });

    }

    /**
     * Method to retrieve all songs that within a week of today
     * @param curDate - the current date
     */
    public void retrieveSongsByDate(String curDate) {

        int firstSlash = curDate.indexOf("/");
        int secondSlash = curDate.lastIndexOf("/");
        int curMonth = Integer.parseInt(curDate.substring(0, firstSlash));
        int curDay = Integer.parseInt(curDate.substring(firstSlash + 1, secondSlash));
        int curYear = Integer.parseInt(curDate.substring(secondSlash + 1, curDate.length()));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference songReference = firebaseDatabase.getReference("Songs");

        // Loop 7 times through the days in the past week
        for (int i = 1; i < DAYS_IN_WEEK; i++) {

            String queryDate = curMonth + "/" + curDay + "/" + curYear;

            Query queryRef = songReference.orderByChild("lastDate").equalTo(queryDate);

            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // Add the song key to the Arraylist of queried songs
                    DatabaseEntry data = dataSnapshot.getValue(DatabaseEntry.class);
                    String songKey = data.getUsername() + "_" + data.getTitle() +"_" + data.getArtist();
                    queriedSongs.add(songKey);

                    Log.d("Retrieve Songs By Date", "Retrieved song titled " + data.getTitle());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            // Rolls the date back one day, might move this to UserInfo later
            Calendar cal = Calendar.getInstance();
            cal.set(curYear, curMonth - 1, curDay);
            cal.add(Calendar.DAY_OF_YEAR, -1);
            curDay = cal.get(Calendar.DATE);
            curMonth = cal.get(Calendar.MONTH) + 1;
            curYear = cal.get(Calendar.YEAR);

        }
    }

    /**
     * Method to query the database for all songs that have been played by friends
     * @param friends - the list of the user's friends
     */
    public void retrieveSongsByFriend(ArrayList<String> friends) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference songReference = firebaseDatabase.getReference("Songs");

        // Loop through the list of all friends
        for (String friend : friends) {

            Query queryRef = songReference.orderByChild("username").equalTo(friend);

            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // Add the song key to the Arraylist of queried songs
                    DatabaseEntry data = dataSnapshot.getValue(DatabaseEntry.class);
                    String songKey = data.getUsername() + "_" + data.getTitle() + "_" + data.getArtist();
                    queriedSongs.add(songKey);

                    Log.d("Retrieve Songs By Friends", "Retrieved song titled " + data.getTitle());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /**
     * Getter for the list of queried songs
     * @return the list of queried songs
     */
    public ArrayList<String> getQueriedSongs() { return queriedSongs; }

}
