package tests;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Toast;

import com.example.team13.flashbackmusic.DatabaseCommunicator;
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
        song = new Song("Title", "Artist", "Album", 0, "0/0",0);
        song.setData(42.1, 60.0, "Tuesday", "9:49", "9/4/18");
    }

    @Test
    public void testSend()
    {

        DatabaseCommunicator dc = new DatabaseCommunicator();
        dc.send(song);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Query queryRef = databaseReference.orderByChild("lastLatitude").equalTo(35);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null)
                    Log.d("Querying data", "No data found");
                else
                    Log.d("Querying Data", "Data found");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Faile to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
            }
        });
    }
}
