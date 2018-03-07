package tests;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Toast;

import com.example.team13.flashbackmusic.DatabaseCommunicator;
import com.example.team13.flashbackmusic.DatabaseEntry;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class TestDatabaseSend {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    private Song song;

    @Before
    public void setUp() {
        song = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "0/0",0);
        song.setData(24.1, 60.0, "Wednesday", "15:45", "3/6/18");
    }

    @Test
    public void testSendNewSong()
    {
        DatabaseCommunicator dc = new DatabaseCommunicator();
        dc.send(song);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Query queryRef = databaseReference.orderByChild("title").equalTo("Allstar");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null) {
                    Log.d("Querying data", "No data found");
                    assert false;
                }
                else {
                    Log.d("Querying Data", "Data found");
                    assert checkDatabaseEntryFields((DatabaseEntry) snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Faile to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
                assert false;
            }
        });
        assert false;
    }

    @Test
    public void testSendUpdateSong()
    {
        DatabaseCommunicator dc = new DatabaseCommunicator();
        song.setData(25.1, 60.0, "Sunday", "17:45", "2/28/18");

        dc.send(song);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Query queryRef = databaseReference.orderByChild("title").equalTo("Allstar");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null) {
                    Log.d("Querying data", "No data found");
                    assert false;
                }
                else {
                    Log.d("Querying Data", "Data found");
                    assert checkDatabaseEntryFields((DatabaseEntry) snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Faile to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
                assert false;
            }
        });
        assert false;
    }

    private boolean checkDatabaseEntryFields(DatabaseEntry databaseEntry)
    {
        return  databaseEntry.getTitle().equals(song.getTitle()) &&
                databaseEntry.getArtist().equals(song.getArtist()) &&
                databaseEntry.getAlbumName().equals(song.getAlbumName()) &&
                databaseEntry.getLastDate().equals(song.getLastDate()) &&
                databaseEntry.getLastDay().equals(song.getLastDay()) &&
                databaseEntry.getLastTime().equals(song.getLastTime()) &&
                databaseEntry.getLastLatitude() == song.getLastLatitude() &&
                databaseEntry.getLastLongitude() == song.getLastLongitude() &&
                databaseEntry.getTrackNumber() == song.getTrack();
        //TODO: check for username and url
    }
}
