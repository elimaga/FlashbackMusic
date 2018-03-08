package com.example.team13.flashbackmusic;

import android.location.Location;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class DatabaseMediator {

    final double KILOMETERS_IN_THOUSAND_FEET = 0.3048;
    final int DAYS_IN_WEEK = 7;
    ArrayList<String> queriedSongs;

    public DatabaseMediator()
    {
        queriedSongs = new ArrayList<>();
    }

    /**
     * Sends Song/Location data to Google Firebase database
     * @param song - Song object containing the data we want to send
     */
    public void send(Song song)
    {
        String username = "usr1";
        String url = "";
        String databaseKey = username + "_" + song.getTitle() + "_" + song.getArtist();

        DatabaseEntry databaseEntry = new DatabaseEntry();
        databaseEntry.setTitle(song.getTitle());
        databaseEntry.setArtist(song.getArtist());
        databaseEntry.setAlbumName(song.getAlbumName());
        databaseEntry.setLastDay(song.getLastDay());
        databaseEntry.setLastTime(song.getLastTime());
        databaseEntry.setLastDate(song.getLastDate());
        databaseEntry.setURL(url);

        List<Double> locList = new ArrayList<Double>();
        locList.add(song.getLastLatitude());
        locList.add(song.getLastLongitude());

        databaseEntry.setLastLocation(locList);
        databaseEntry.setTrackNumber(song.getTrack());
        databaseEntry.setUsername(username);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference songReference = firebaseDatabase.getReference("Songs");
        DatabaseReference locationReference = firebaseDatabase.getReference("Locations");
        GeoFire geoFire = new GeoFire(locationReference);

        songReference.child(databaseKey).setValue(databaseEntry, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                Log.d("Send GeoLocation","Value was set. Error = "+error);
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
    public void retrieveLocation(double latitude, double longitude) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference locationReference = firebaseDatabase.getReference("Locations");
        GeoFire geoFire = new GeoFire(locationReference);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), KILOMETERS_IN_THOUSAND_FEET);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d("Retrive GeoLocation", String.format("Key %s is within 1000 feet at [%f,%f]", key, location.latitude, location.longitude));
                queriedSongs.add(key);
            }

            @Override
            public void onKeyExited(String key) {
                Log.d("Retrive GeoLocation", String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("Retrive GeoLocation", String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                Log.d("Retrive GeoLocation", "All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d("Retrive GeoLocation", "Error: " + error);
            }
        });
    }

    /**
     * Method to retrieve all songs that within a week of today
     * @param curDate - the current date
     */
    public void retrieveDate(String curDate) {

        int firstSlash = curDate.indexOf("/");
        int secondSlash = curDate.lastIndexOf("/");
        int curMonth = Integer.parseInt(curDate.substring(0, firstSlash));
        int curDay = Integer.parseInt(curDate.substring(firstSlash + 1, secondSlash));
        int curYear = Integer.parseInt(curDate.substring(secondSlash + 1, curDate.length()));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference songReference = firebaseDatabase.getReference("Songs");

        for (int i = 0; i < DAYS_IN_WEEK; i++) {

            String queryDate = curMonth + "/" + curDay + "/" + curYear;
            System.out.println(queryDate);


            Query queryRef = songReference.orderByChild("lastDate").equalTo(queryDate);

            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    System.out.println(dataSnapshot.getValue(DatabaseEntry.class).getTitle());
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

            Calendar cal = Calendar.getInstance();
            cal.set(curYear, curMonth, curDay);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            curDay = cal.get(Calendar.DATE);
            curMonth = cal.get(Calendar.MONTH) + 1;
            curYear = cal.get(Calendar.YEAR);

            System.out.println(queryDate);


        }
    }

    /**
     * Getter for the list of queried songs
     * @return the list of queried songs
     */
    public ArrayList<String> getQueriedSongs() {
        return this.queriedSongs;
    }

}
