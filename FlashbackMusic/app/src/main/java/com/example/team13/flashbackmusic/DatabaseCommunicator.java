package com.example.team13.flashbackmusic;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class DatabaseCommunicator {

    public DatabaseCommunicator()
    {
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
        databaseEntry.setLastLatitude(song.getLastLatitude());
        databaseEntry.setLastLongitude(song.getLastLongitude());
        databaseEntry.setTrackNumber(song.getTrack());
        databaseEntry.setUsername(username);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        GeoFire geoFire = new GeoFire(databaseReference);

        databaseReference.child(databaseKey).setValue(databaseEntry, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                Log.d("Send Method","Value was set. Error = "+error);
            }
        });

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
