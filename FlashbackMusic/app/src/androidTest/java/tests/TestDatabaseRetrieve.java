package tests;

import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.DatabaseCommunicator;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.Song;
import com.firebase.geofire.GeoLocation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Elijah Magallanes on 3/1/2018.
 */

public class TestDatabaseRetrieve {

        @Rule
        public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

        @Before
        public void setUp() {
        }

        @Test
        public void testRetrieve()
        {
            DatabaseCommunicator dc = new DatabaseCommunicator();
            dc.retrieve(new GeoLocation(29.56, -42.89));

            assert true;
        }

}
