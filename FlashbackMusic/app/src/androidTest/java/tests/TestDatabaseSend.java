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
    DatabaseMediator databaseMediator;
    Song song;

    @Before
    public void setUp() {
        song = new Song("Killer Queen", "Queen", "Hereafter", 0, "0/0",0);
        song.setData(49.0, 25.0, "Monday", "11:15", "3/1/18");
        databaseMediator = new DatabaseMediator();
    }

    @Test
    public void testSend()
    {
        databaseMediator.send(song);
        assert true;
    }
}
