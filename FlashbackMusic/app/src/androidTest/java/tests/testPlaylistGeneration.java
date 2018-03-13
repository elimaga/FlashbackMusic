package tests;

import java.lang.Math;

import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.FlashbackPlaylist;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by andrewyu on 2/17/18.
 */

public class testPlaylistGeneration {

    final float METERS_IN_THOUSAND_FEET = 304.8f;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    FlashbackPlaylist flashbackPlaylist;

    @Before
    public void setUp()
    {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        int[] resourceIds = {R.raw._123_go, R.raw.america_religious, R.raw.beautiful_pain,
                R.raw.cant_you_be_mine, R.raw.i_will_not_be_afraid, R.raw.stomp_jump_boogie};
        //mainActivity.getActivity().loadLibrary(mediaMetadataRetriever, resourceIds);
        double[] mockLocationCoordinates = {100, 100};
        flashbackPlaylist = new FlashbackPlaylist(
                mainActivity.getActivity().getSongs(),
                mockLocationCoordinates,
                "Saturday",
                "13:57", "2/15/17");

    }


    /**
     * Testing to see if FlashbackPlaylist can detect that our current location is
     * exactly at the last known location stored in a song
     */
    @Test
    public void testMatchesExactLocation() {
        assert flashbackPlaylist.matchesLocation(
                100, 100,
                100, 100);
    }

    /**
     * Testing to see if FlashbackPlaylist can detect that our current location is
     * 1000 ft from the last known location stored in a song
     */
    @Test
    public void testMatchesEdgeLocation() {
        assert flashbackPlaylist.matchesLocation(
                100, 100 + METERS_IN_THOUSAND_FEET,
                100, 100);
    }

    /**
     * Testing to see if FlashbackPlaylist can detect that our current location is
     * 1001 ft from the last known location stored in a song and returns false
     */
    @Test
    public void testDoesNotMatchesLocationOnOutside() {
        assert !flashbackPlaylist.matchesLocation(
                100, 100 + METERS_IN_THOUSAND_FEET + 1,
                100, 100);
    }

    /**
     * Testing to see if FlashbackPlaylist can detect that our current location is
     * 1000 ft from the last known location stored in a song (using a 45-45-90 triangle)
     */
    @Test
    public void testMatchesLocation() {
        assert flashbackPlaylist.matchesLocation(
                100 + (METERS_IN_THOUSAND_FEET / Math.sqrt(2)),
                100 + (METERS_IN_THOUSAND_FEET / Math.sqrt(2)),
                100, 100);
    }

    /**
     * Testing to see if FlashbackPlaylist can match days of the week
     */
    @Test
    public void testMatchesSameDay() {
        assert flashbackPlaylist.matchesDay("Saturday", "Saturday");
    }

    /**
     * Testing to see if FlashbackPlaylist doesn't match one day of the week to another
     */
    @Test
    public void testDoesNotMatchesDifferentDay() {
        assert !flashbackPlaylist.matchesDay("Saturday", "Sunday");
    }

    /**
     * Testing to see if FlashbackPlaylist matches a time with itself
     */
    @Test
    public void testMatchesSameTimeOfDay() {
        assert flashbackPlaylist.matchesTimeOfDay("13:57", "13:57");
    }

    /**
     * Testing to see if FlashbackPlaylist matches a time between 5-11am with another time between
     * 5-11am
     */
    @Test
    public void testMatchesPeriodOfDay() {
        assert flashbackPlaylist.matchesTimeOfDay("8:21", "10:57");
    }

    /**
     * Testing to see if FlashbackPlaylist does not match a time between 5am - 11am with
     * a time between 5pm - 5am
     */
    @Test
    public void testDoesNotMatchesDifferentPeriodsOfDay() {
        assert !flashbackPlaylist.matchesTimeOfDay("8:21", "20:57");
    }
}
