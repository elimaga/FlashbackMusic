package tests;

import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.DatabaseCommunicator;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Andrew Yu and Elijah Magallanes on 3/1/18.
 */

public class TestDatabaseSend {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() {
    }

    @Test
    public void testSend()
    {
        Song song = new Song("3005", "Childish Gambino", "Unknown", 0, "0/0",0);
        song.setData(69.0, -80.0, "Monday", "10:50", "4/6/18");
        DatabaseCommunicator dc = new DatabaseCommunicator();
        dc.send(song);

        assert true;
    }
}
