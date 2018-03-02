package com.example.team13.flashbackmusic;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by andrewyu on 3/1/18.
 */

public class DatabaseCommunicator {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GeoFire geoFire;

    public DatabaseCommunicator()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        geoFire = new GeoFire(databaseReference);
    }

    /**
     *
     * @param song
     */
    public void send(Song song)
    {
        String username = "usr1";
        String url = "";
        String databaseKey = username + "-" + song.getTitle() + "-" + song.getArtist();

        DatabaseEntry databaseEntry = new DatabaseEntry(song, url, username);

        databaseReference.child(databaseKey).setValue(databaseEntry);
        //geoFire.setLocation(databaseKey, new GeoLocation(song.getLastLatitude(), song.getLastLongitude()));

        geoFire.setLocation(databaseKey + "-location", new GeoLocation(song.getLastLatitude(), song.getLastLongitude()),
                new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    Log.d("Send method", "There was an error saving the location to GeoFire: " + error);
                } else {
                    Log.d("Send method","Location saved on server successfully!");
                }
            }
        });
    }
}
