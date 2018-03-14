package tests;

import java.util.ArrayList;

import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.VibeModePlaylist;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by andrewyu on 2/17/18.
 */
public class TestPlaylistGeneration {

    private static final int DEFAULT_COORDINATE = 45;
    private static final String TODAY = "1/1/2011";
    private static final String ONE_YEAR_AGO = "1/1/2010";
    private static final String THE_PAST_WEEK = "12/26/2010";
    private static final String ONE_WEEK_AGO = "12/25/2010";
    private static final double KM_IN_THOUSAND_FEET = 0.3048;
    private static final double ONE_KM_IN_DEGREES = 1 / 111.32; // ~= 0.0000089
    private static final double THOUSAND_FEET_IN_DEGREES = ONE_KM_IN_DEGREES * KM_IN_THOUSAND_FEET;
    private static final String FRIEND = "Gary";
    private static final String STRANGER = "George";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);
    VibeModePlaylist vibeModePlaylist;

    @Before
    public void setUp()
    {
        double[] location = {DEFAULT_COORDINATE, DEFAULT_COORDINATE};
        ArrayList<String> friends =  new ArrayList<>();
        friends.add(FRIEND);
        vibeModePlaylist = new VibeModePlaylist(location, TODAY, friends);
    }


    /**
     * Testing to see if VibeModePlaylist can detect that our current location is
     * exactly at the last known location stored in a song
     */
    @Test
    public void testMatchesExactLocation() {
        assertEquals(true, vibeModePlaylist.matchesLocation(DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                DEFAULT_COORDINATE, DEFAULT_COORDINATE));
    }

    /**
     * Testing to see if VibeModePlaylist can detect that our current location is
     * 1000 ft from the last known location stored in a song
     */
    @Test
    public void testMatchesEdgeLocation() {
        assertEquals(true, vibeModePlaylist.matchesLocation(DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                DEFAULT_COORDINATE - THOUSAND_FEET_IN_DEGREES,
                DEFAULT_COORDINATE));
    }

    /**
     * Testing to see if VibeModePlaylist can detect that our current location is
     * outside 1000 ft from the last known location stored in a song and returns false
     */
    @Test
    public void testDoesNotMatchesLocationOnOutside() {
        assertEquals(false, vibeModePlaylist.matchesLocation(DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                DEFAULT_COORDINATE - THOUSAND_FEET_IN_DEGREES,
                DEFAULT_COORDINATE - THOUSAND_FEET_IN_DEGREES));
    }

    /**
     * Testing to see if VibeModePlaylist can detect that our current location is
     * 1000 ft from the last known location stored in a song (using a 45-45-90 triangle)
     */
    @Test
    public void testMatchesLocation() {
        assertEquals(true, vibeModePlaylist.matchesLocation(DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                DEFAULT_COORDINATE - THOUSAND_FEET_IN_DEGREES / Math.sqrt(2),
                DEFAULT_COORDINATE - THOUSAND_FEET_IN_DEGREES / Math.sqrt(2)));
    }

    /**
     * Testing to see if VibeModePlaylist can match Today with itself
     */
    @Test
    public void testMatchesSameDay() {
        assertEquals(true, vibeModePlaylist.matchesDate(TODAY));
    }

    /**
     * Testing to see if VibeModePlaylist doesn't match Today with a day more than 7 days ago
     */
    @Test
    public void testDoesNotMatchesDifferentDay() {
        assertEquals(false, vibeModePlaylist.matchesDate(ONE_WEEK_AGO));
    }

    /**
     * Testing to see if VibeModePlaylist matches a time with itself
     */
    @Test
    public void testDoesNotMatchOneYearAgo() {
        assertEquals(false, vibeModePlaylist.matchesDate(ONE_YEAR_AGO));
    }

    /**
     * Testing to see if VibeModePlaylist matches a user with their friend
     */
    @Test
    public void testMatchesFriends() {
        assertEquals(true, vibeModePlaylist.matchesFriend(FRIEND));
    }

    /**
     * Testing to see if VibeModePlaylist does not match a user with a stranger
     */
    @Test
    public void testDoesNotMatchStranger() {
        assertEquals(false, vibeModePlaylist.matchesFriend(STRANGER));
    }
}

