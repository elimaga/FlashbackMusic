package com.example.team13.flashbackmusic;

import android.location.Location;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class DatabaseMediator {

    final float KILOMETERS_IN_THOUSAND_FEET = 0.3048f;
    ArrayList<String> queriedSongs;

    public DatabaseMediator()
    {
        queriedSongs = new ArrayList<>();
    }

    /**
     *
     * @param song
     */
    public void send(Song song) {
        String username = "usr1";
        String url = "";
        String databaseKey = username + "-" + song.getTitle() + "-" + song.getArtist();


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        GeoFire geoFire = new GeoFire(databaseReference);

        DatabaseEntry databaseEntry = new DatabaseEntry(song, url, username);

        databaseReference.child(databaseKey).setValue(databaseEntry);
        //geoFire.setLocation(databaseKey, new GeoLocation(song.getLastLatitude(), song.getLastLongitude()));

        geoFire.setLocation("location", new GeoLocation(song.getLastLatitude(), song.getLastLongitude()),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.out.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });
    }

    /**
     * Method to retrieve all songs that are within 1000 feet of the user's location
     * @param currentLocation - the user's current location
     */
    public void retrieveLocation(GeoLocation currentLocation) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        GeoFire geoFire = new GeoFire(databaseReference);

        GeoQuery geoQuery = geoFire.queryAtLocation(currentLocation, KILOMETERS_IN_THOUSAND_FEET);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s is within 1000 feet at [%f,%f]", key, location.latitude, location.longitude));
                // queriedSongs.add(key.substring(0,key.indexOf("-")));
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("Error: " + error);
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
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        int minDay = curDay - 6;

        String startDate = curMonth + "/" + minDay + "/" + curYear;
        //String endDate = curMonth + "/" + curDay + "/" + curYear;

        Query queryRef = databaseReference.orderByChild("lastDate").equalTo(startDate);

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                System.out.println(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //TODO: loop through rest of dates

    }

    /**
     * Getter for the list of queried songs
     * @return the list of queried songs
     */
    public ArrayList<String> getQueriedSongs() {
        return this.queriedSongs;
    }

}
