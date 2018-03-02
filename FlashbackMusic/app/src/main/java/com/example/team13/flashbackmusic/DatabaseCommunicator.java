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

/**
 * Created by andrewyu on 3/1/18.
 */

public class DatabaseCommunicator {

    final float KILOMETERS_IN_THOUSAND_FEET = 0.3048f;

    public DatabaseCommunicator()
    {
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

    public void retrieve(GeoLocation currentLocation) {
        String username = "usr1";
        String url = "";

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        GeoFire geoFire = new GeoFire(databaseReference);

        GeoQuery geoQuery = geoFire.queryAtLocation(currentLocation, KILOMETERS_IN_THOUSAND_FEET );

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
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
                System.err.println("There was an error with this query: " + error);
            }
        });
    }
}
