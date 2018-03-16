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
 * Created by andrewyu on 3/13/18.
 */

public class TestVibeModeSorting {
    private static final int DEFAULT_COORDINATE = 90;
    private static final String TODAY = "1/1/2011";
    private static final String ONE_YEAR_AGO = "1/1/2010";
    private static final String THE_PAST_WEEK = "12/26/2010";
    private static final float METERS_IN_THOUSAND_FEET = 304.8f;
    private static final String FRIEND_NAME = "Gary";
    private static final String FRIEND_ID = "1";
    private static final String STRANGER_NAME = "George";
    private static final String STRANGER_ID = "0";


    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    VibeModePlaylist vibeModePlaylist;
    ArrayList<Song> correct;

    @Before
    public void setUp()
    {
        double[] location = {DEFAULT_COORDINATE, DEFAULT_COORDINATE};
        Set<String> friends =  new HashSet<>();
        friends.add(FRIEND_ID);
        vibeModePlaylist = new VibeModePlaylist(location, TODAY, friends);

        correct = new ArrayList<>();
    }

    @Test
    public void testSortTimeTieBreaker()
    {

        Song song1 = new Song("Don't Stop Me Now", "Queen", "Jazz", 0, "www", 0,
                "Tuesday", "21:20", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        Song song2 = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "www", 0,
                "Tuesday", "21:19", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        //Set Songs in vibeModePlaylist
        vibeModePlaylist.addSong(song2);
        vibeModePlaylist.addSong(song1);


        //sort Playlist
        vibeModePlaylist.sortPlaylist(TODAY);

        //Set expected sorted playlist
        correct.add(song1);
        correct.add(song2);

        //compare algorithm output with expected correct output
        assertEquals(correct, vibeModePlaylist.getPlaylist());
    }

    @Test
    public void testSortDateTieBreaker()
    {

        Song song1 = new Song("Don't Stop Me Now", "Queen", "Jazz", 0, "www", 0,
                "Tuesday", "21:20", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        Song song2 = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "www", 0,
                "Tuesday", "21:20", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        //Set Songs in vibeModePlaylist
        vibeModePlaylist.addSong(song2);
        vibeModePlaylist.addSong(song1);


        //sort Playlist
        vibeModePlaylist.sortPlaylist(TODAY);

        //Set expected sorted playlist
        correct.add(song1);
        correct.add(song2);

        //compare algorithm output with expected correct output
        assertEquals(correct, vibeModePlaylist.getPlaylist());
    }

    @Test
    public void testSortOneRequirementEach()
    {
        //Satisfies catagory B (Date)
        Song song1 = new Song("Don't Stop Me Now", "Queen", "Jazz", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        //Satisfies catagory A (Location)
        Song song2 = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, ONE_YEAR_AGO);
        //Satisfies catagory C (Friend)
        Song song3 = new Song("Sympathy for the Devil", "The Rolling Stones", "Beggars Banquet", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE / 2, DEFAULT_COORDINATE / 2,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);

        //Set Songs in vibeModePlaylist
        vibeModePlaylist.addSong(song1);
        vibeModePlaylist.addSong(song2);
        vibeModePlaylist.addSong(song3);

        //sort Playlist
        vibeModePlaylist.sortPlaylist(TODAY);

        //Set expected sorted playlist
        correct.add(song2);
        correct.add(song1);
        correct.add(song3);

        //compare algorithm output with expected correct output
        assertEquals(correct, vibeModePlaylist.getPlaylist());
    }

    @Test
    public void testSortTwoRequirements()
    {
        //Satisfies catagories B,C (Date, Friend)
        Song song1 = new Song("Don't Stop Me Now", "Queen", "Jazz", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        //Satisfies catagories A,C (Location, Friend)
        Song song2 = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);
        //Satisfies catagories A,B (Location, Date)
        Song song3 = new Song("Sympathy for the Devil", "The Rolling Stones", "Beggars Banquet", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);

        //Set Songs in vibeModePlaylist
        vibeModePlaylist.addSong(song1);
        vibeModePlaylist.addSong(song2);
        vibeModePlaylist.addSong(song3);

        //sort Playlist
        vibeModePlaylist.sortPlaylist(TODAY);

        //Set expected sorted playlist
        correct.add(song3);
        correct.add(song2);
        correct.add(song1);

        //compare algorithm output with expected correct output
        assertEquals(correct, vibeModePlaylist.getPlaylist());
    }

    @Test
    public void testSortTwoRequirementsAgainstOneRequirement()
    {
        //Satisfies catagories B,C (Date, Friend)
        Song song1 = new Song("Don't Stop Me Now", "Queen", "Jazz", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        //Satisfies catagories A,C (Location, Friend)
        Song song2 = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);
        //Satisfies catagory A (Location)
        Song song3 = new Song("Sympathy for the Devil", "The Rolling Stones", "Beggars Banquet", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, ONE_YEAR_AGO);

        //Set Songs in vibeModePlaylist
        vibeModePlaylist.addSong(song1);
        vibeModePlaylist.addSong(song2);
        vibeModePlaylist.addSong(song3);

        //sort Playlist
        vibeModePlaylist.sortPlaylist(TODAY);

        //Set expected sorted playlist
        correct.add(song2);
        correct.add(song1);
        correct.add(song3);

        //compare algorithm output with expected correct output
        assertEquals(correct, vibeModePlaylist.getPlaylist());
    }

    @Test
    public void testSortPerfectSong()
    {
        //Satisfies catagories B,C (Date, Friend)
        Song song1 = new Song("Don't Stop Me Now", "Queen", "Jazz", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        //Satisfies catagories A,C (Location, Friend)
        Song song2 = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);
        //Satisfies catagory A,B (Location, Date)
        Song song3 = new Song("Sympathy for the Devil", "The Rolling Stones", "Beggars Banquet", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        //Satisfies catagories A,B,C (Location,Date,Time)
        Song song4 = new Song("Numb", "Linken Park", "Meteora", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);

        //Set Songs in vibeModePlaylist
        vibeModePlaylist.addSong(song1);
        vibeModePlaylist.addSong(song2);
        vibeModePlaylist.addSong(song3);
        vibeModePlaylist.addSong(song4);

        //sort Playlist
        vibeModePlaylist.sortPlaylist(TODAY);

        //Set expected sorted playlist
        correct.add(song4);
        correct.add(song3);
        correct.add(song2);
        correct.add(song1);

        //compare algorithm output with expected correct output
        assertEquals(correct, vibeModePlaylist.getPlaylist());
    }

    @Test
    public void testMasterPlaylistGeneration()
    {
        //Satisfies catagories A,B,C (Location,Date,Time)
        Song song0 = new Song("Numb", "Linken Park", "Meteora", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, TODAY);
        //Satisfies catagories B,C (Date, Friend)
        Song song1 = new Song("Don't Stop Me Now", "Queen", "Jazz", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                FRIEND_NAME, FRIEND_ID, THE_PAST_WEEK);
        //Satisfies catagories A,C (Location, Friend)
        Song song2 = new Song("Allstar", "Smash Mouth", "Astro Lounge", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);
        //Satisfies catagory A,B (Location, Date)
        Song song3 = new Song("Sympathy for the Devil", "The Rolling Stones", "Beggars Banquet", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        //Satisfies catagory B (Date)
        Song song4 = new Song("Roundabout", "Yes", "Fragile", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE/2, DEFAULT_COORDINATE/2,
                STRANGER_NAME, STRANGER_ID, THE_PAST_WEEK);
        //Satisfies catagory A (Location)
        Song song5 = new Song("Ocean Man", "Ween", "The Mollusk", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE, DEFAULT_COORDINATE,
                STRANGER_NAME, STRANGER_ID, ONE_YEAR_AGO);
        //Satisfies catagory C (Friend)
        Song song6 = new Song("All Along The Watchtower", "Jimi Hendrix", "Isle of Wight", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE / 2, DEFAULT_COORDINATE / 2,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);
        //Satisfies no catagories
        Song song7 = new Song("Hound Dog", "Elvis Presley", "From Vegas to Memphis", 0, "www", 0,
                "Tuesday", "21:21", DEFAULT_COORDINATE / 2, DEFAULT_COORDINATE / 2,
                FRIEND_NAME, FRIEND_ID, ONE_YEAR_AGO);

        //Set Songs in vibeModePlaylist
        vibeModePlaylist.addSong(song0);
        vibeModePlaylist.addSong(song1);
        vibeModePlaylist.addSong(song2);
        vibeModePlaylist.addSong(song3);
        vibeModePlaylist.addSong(song4);
        vibeModePlaylist.addSong(song5);
        vibeModePlaylist.addSong(song6);
        vibeModePlaylist.addSong(song7);

        //sort Playlist
        vibeModePlaylist.sortPlaylist(TODAY);

        //Set expected sorted playlist
        correct.add(song0);
        correct.add(song3);
        correct.add(song2);
        correct.add(song1);
        correct.add(song5);
        correct.add(song4);
        correct.add(song6);
        correct.add(song7);

        //compare algorithm output with expected correct output
        assertEquals(correct, vibeModePlaylist.getPlaylist());
    }
}
