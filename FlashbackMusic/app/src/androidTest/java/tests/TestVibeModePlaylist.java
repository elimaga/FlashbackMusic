package tests;

import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.FlashbackPlaylist;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.MusicLibrary;
import com.example.team13.flashbackmusic.Song;
import com.example.team13.flashbackmusic.VibeModePlaylist;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by andrewyu on 3/12/18.
 */

public class TestVibeModePlaylist {
    public static final int DEFAULT_COORDINATE = 90;
    final float METERS_IN_THOUSAND_FEET = 304.8f;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    VibeModePlaylist vibeModePlaylist;
    Song song1;
    Song song2;

    @Before
    public void setUp()
    {
        double[] location = {DEFAULT_COORDINATE, DEFAULT_COORDINATE};
        ArrayList<String> friends =  new ArrayList<>();
        friends.add("Gary");
        vibeModePlaylist = new VibeModePlaylist(location, "1/1/2011", friends, MusicLibrary.getInstance(mainActivity.getActivity()));

        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
        "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
        "Gary", "1/1/2011");
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                "Gary", "1/1/2011");
    }

    @Test
    public void testHasSamePriority()
    {
        assertEquals(false, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testHasHigherLocationPriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                "Gary", "1/1/2011");
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", 0, 0,
                "Gary", "1/1/2011");
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testHasHigherDatePriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                "Gary", "1/1/2011");
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                "Gary", "1/1/2010");
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testHasHigherFriendPriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                "Gary", "1/1/2011");
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                "George", "1/1/2011");
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testFarFriendVSNearStrangerPriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                "Gary", "1/1/2011");
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                "George", "1/1/2011");
        assertEquals(false, vibeModePlaylist.hasHigherPriority(song1, song2));
    }
}
