package tests;

import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.DatabaseMediator;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by andrewyu on 3/1/18.
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
        Song song = new Song("Killer Queen", "Queen", "Killer Queen (album)", 0, "0/0",0);
        song.setData(60.0, 100.0, "Monday", "11:15", "3/1/18");
        DatabaseMediator databaseMediator = new DatabaseMediator();
        databaseMediator.send(song);

        assert true;
    }
}
