package tests;

import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.Song;
import com.example.team13.flashbackmusic.VibeModePlaylist;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * Created by andrewyu on 3/12/18.
 */

public class TestHasHigherPriority {
    private static final int DEFAULT_COORDINATE = 90;
    private static final String TODAY = "1/1/2011";
    private static final String ONE_YEAR_AGO = "1/1/2010";
    private static final String THE_PAST_WEEK = "12/26/2010";
    private static final String FRIEND_NAME = "Gary";
    private static final String FRIEND_ID = "1";
    private static final String STRANGER_NAME = "George";
    private static final String STRANGER_ID = "0";


    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    VibeModePlaylist vibeModePlaylist;
    Song song1;
    Song song2;

    @Before
    public void setUp()
    {
        double[] location = {DEFAULT_COORDINATE, DEFAULT_COORDINATE};
        Set<String> friends =  new HashSet<>();
        friends.add(FRIEND_ID);
        vibeModePlaylist = new VibeModePlaylist(location, TODAY, friends);

        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
        "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
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
                FRIEND_NAME, FRIEND_ID, TODAY);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", 0, 0,
                FRIEND_NAME, FRIEND_ID, TODAY);
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testHasHigherDatePriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testHasHigherFriendPriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, TODAY);
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testFarFriendVSNearStrangerPriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                FRIEND_NAME, FRIEND_ID, TODAY);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, TODAY);
        assertEquals(false, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testFarFriendInThePastVSNearStrangerInThePastWeekPriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        assertEquals(false, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testFarStrangerVSNearStrangerBothInThePastWeekPriority()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        assertEquals(false, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testWithinThePastWeek()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, ONE_YEAR_AGO);
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testTimeTieBreaker()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "0:16", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "0:09", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testDateTieBreaker()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }

    @Test
    public void testDateAndTimeTieBreaker()
    {
        song1 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Tuesday", "21:20", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        song2 = new Song("Title", "Artist", "album", 0, "www", 0,
                "Monday", "21:19", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        assertEquals(true, vibeModePlaylist.hasHigherPriority(song1, song2));
    }
}
