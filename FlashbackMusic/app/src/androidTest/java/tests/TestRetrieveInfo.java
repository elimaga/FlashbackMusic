package tests;

import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Eli on 2/17/2018.
 */

public class TestRetrieveInfo {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    final double INVALID_COORDINATE = 200.0;

    @Before
    public void setUp()
    {
        Song song1 = new Song("Crane City", "", "", 0, "0/", 0 );
        Song song2 = new Song("mojo potion #61/49", "", "", 0, "0/", 1);
        Song song3 = new Song("Tightrope Walker", "", "", 0, "0/", 2);
        SharedPreferences sharedPreferences = mainActivity.getActivity().getSharedPreferences("flashback", mainActivity.getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String title1 = song1.getTitle();
        String title2 = song2.getTitle();
        double latitude1 = 50.0;
        double latitude2 = INVALID_COORDINATE;
        double longitude1 = -100.0;
        double longitude2 = INVALID_COORDINATE;
        String day1 = "Monday";
        String day2 = "Tuesday";
        String date1 = "12/25/17";
        String date2 = "5/4/68";
        String time1 = "3:54";
        String time2 = "14:32";

        editor.putString(title1 + "_latitude", "" + latitude1);
        editor.putString(title1 + "_longitude", "" + longitude1);
        editor.putString(title1 + "_day", day1);
        editor.putString(title1 + "_date", date1);
        editor.putString(title1 + "_time", time1);

        editor.putString(title2 + "_latitude", "" + latitude2);
        editor.putString(title2 + "_longitude", "" + longitude2);
        editor.putString(title2 + "_day", day2);
        editor.putString(title2 + "_date", date2);
        editor.putString(title2 + "_time", time2);

        editor.apply();

    }


    /**
     * Testing to see if the new historical information about the song was loaded correctly from the
     * SharedPreferences where the location is valid.
     */
    @Test
    public void testRetrieveInfoValidLocation() {
        Song song = new Song("Crane City", "", "", 0, "0/", 0 );
        mainActivity.getActivity().retrieveInfo(song);
        assertEquals(50.0, song.getLastLatitude());
        assertEquals(-100.0, song.getLastLongitude());
        assertEquals("Monday", song.getLastDay());
        assertEquals("12/25/17", song.getLastDate());
        assertEquals("3:54", song.getLastTime());
        assertEquals("Evening", song.getLastSetting());
    }

    /**
     * Testing to see if the new historical information about the song was loaded correctly from the
     * SharedPreferences where the location is invalid.
     */
    @Test
    public void testRetrieveInfoInvalidLocation() {
        Song song = new Song("mojo potion #61/49", "", "", 0, "0/", 1);
        mainActivity.getActivity().retrieveInfo(song);
        assertEquals(INVALID_COORDINATE, song.getLastLatitude());
        assertEquals(INVALID_COORDINATE, song.getLastLongitude());
        assertEquals("Tuesday", song.getLastDay());
        assertEquals("5/4/68", song.getLastDate());
        assertEquals("14:32", song.getLastTime());
        assertEquals("Afternoon", song.getLastSetting());
    }

    /**
     * Testing to see if the historical information about the song was loaded correctly from the
     * SharedPreferences when there is no previous info
     */
    @Test
    public void testRetrieveNoInfo() {
        Song song = new Song("Tightrope Walker", "", "", 0, "0/", 2);
        mainActivity.getActivity().retrieveInfo(song);
        assertEquals(INVALID_COORDINATE, song.getLastLatitude());
        assertEquals(INVALID_COORDINATE, song.getLastLongitude());
        assertEquals("", song.getLastDay());
        assertEquals("", song.getLastDate());
        assertEquals("", song.getLastTime());
        assertEquals("", song.getLastSetting());
    }

}
