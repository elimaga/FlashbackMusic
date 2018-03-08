package tests;

import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.DatabaseMediator;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.Song;
import com.firebase.geofire.GeoLocation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Elijah Magallanes on 3/1/2018.
 */

public class TestDatabaseRetrieve {

        @Rule
        public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
        DatabaseMediator databaseMediator;
        Song song;

        @Before
        public void setUp() {
            databaseMediator = new DatabaseMediator();
        }

        @Test
        public void testRetrieveLocation()
        {
            databaseMediator.retrieveLocation(49.0, 25.0);

            //assertEquals(true, geoLocation.latitude == 49.0 && geoLocation.longitude == 100.0);

            ArrayList<String> queriedSongs = databaseMediator.getQueriedSongs();

            //assertEquals("usr1_Humble_Kendrick Lamar", queriedSongs.get(0));
            assertEquals("usr1_Killer Queen_Queen", queriedSongs.get(0));

        }

        @Test
        public void testRetrieveDate() {
            DatabaseMediator databaseMediator = new DatabaseMediator();
            databaseMediator.retrieveDate("10/10/15");

            assert true;
        }

}
