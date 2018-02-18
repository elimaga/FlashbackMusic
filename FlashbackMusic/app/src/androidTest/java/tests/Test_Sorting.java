package tests;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.team13.flashbackmusic.FlashbackPlaylist;
import com.example.team13.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by rolandkong on 2/14/18.
 */

public class Test_Sorting {

    FlashbackPlaylist list;
    ArrayList<Song> songs;

//    @Rule
//    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Before
    public void setUp(){
        songs = new ArrayList<>();
        Song song = new Song("Third","he","ll",0,"2/11", 0);
        song.setData(0,0, "MONDAY", "11:45", "11/15/18");
        songs.add(song);
<<<<<<< HEAD
        song = new Song("First","he","ll", 0,"3/11", 0);
=======
        song = new Song("First","he","ll",0,"3/11", 0);
>>>>>>> ffa88f12c79a8c0589b6b2aae1621a2eaf58f715
        song.setData(0,0, "MONDAY", "23:30", "11/15/18");
        songs.add(song);
        song = new Song("Second","he","ll",0,"4/11", 0);
        song.setData(0,0, "MONDAY", "17:15", "11/15/18");
        songs.add(song);
<<<<<<< HEAD
        song = new Song("Fourth","he","ll", 0,"5/11", 0);
=======
        song = new Song("Fourth","he","ll",0,"5/11", 0);
>>>>>>> ffa88f12c79a8c0589b6b2aae1621a2eaf58f715
        song.setData(0,0, "MONDAY", "1:02", "11/15/18");
        songs.add(song);
    }

    @Test
    public void test_compare_time(){

        int check = FlashbackPlaylist.compareTimes(songs.get(0).getLastTime());
        assertEquals(735, check);
        check = FlashbackPlaylist.compareTimes(songs.get(1).getLastTime());
        assertEquals(30, check);
        check = FlashbackPlaylist.compareTimes(songs.get(2).getLastTime());
        assertEquals(405, check);
        check = FlashbackPlaylist.compareTimes(songs.get(3).getLastTime());
        assertEquals(1378, check);

    }

    @Test
    public void test_compare_date(){
        songs.get(0).setData(0,0, "MONDAY", "11:30", "11/15/17");
        songs.get(1).setData(0,0, "MONDAY", "11:30", "1/4/16");
        songs.get(2).setData(0,0, "MONDAY", "11:30", "6/30/16");
        songs.get(3).setData(0,0, "MONDAY", "11:30", "7/13/11");
        int check = FlashbackPlaylist.compareDates(songs.get(0).getLastDate(),"2/15/18");
        assertEquals(95, check);
        check = FlashbackPlaylist.compareDates(songs.get(1).getLastDate(),"2/15/18");
        assertEquals(771, check);
        check = FlashbackPlaylist.compareDates(songs.get(2).getLastDate(),"2/15/18");
        assertEquals(595, check);
        check = FlashbackPlaylist.compareDates(songs.get(3).getLastDate(),"2/15/18");
        assertEquals(2407, check);

    }

    @Test
    public void test_break_time_ties() {
        ArrayList<Song> tiesBroken = FlashbackPlaylist.breakTimeTies(songs);
        assertEquals("First", tiesBroken.get(0).getTitle());
        assertEquals("Second", tiesBroken.get(1).getTitle());
        assertEquals("Third", tiesBroken.get(2).getTitle());
        assertEquals("Fourth", tiesBroken.get(3).getTitle());
    }

    @Test
    public void test_break_date_ties() {
        songs.get(0).setData(0,0, "MONDAY", "11:30", "3/15/16");
        songs.get(1).setData(0,0, "MONDAY", "11:30", "1/4/18");
        songs.get(2).setData(0,0, "MONDAY", "11:30", "6/30/16");
        songs.get(3).setData(0,0, "MONDAY", "11:30", "7/13/11");
        ArrayList<Song> tiesBroken = FlashbackPlaylist.breakDateTies(songs, "2/16/18");
        assertEquals("First", tiesBroken.get(0).getTitle());
        assertEquals("Second", tiesBroken.get(1).getTitle());
        assertEquals("Third", tiesBroken.get(2).getTitle());
        assertEquals("Fourth", tiesBroken.get(3).getTitle());
    }

    @Test
    public void test_sort_playlist_without_ties() {
        double[] location = new double[2];
        location[0] = 0;
        location[1] = 0;
        Song song = new Song("3.5", "", "", 0, "3/11", 0);
        songs.add(song);
        song = new Song("1.5", "", "", 0, "3/11", 0);
        songs.add(song);
        songs.get(1).setData(0,0, "MONDAY", "11:30", "1/4/18");
        songs.get(5).setData(0,0, "MONDAY", "11:30", "11/4/17");
        songs.get(2).setData(0,0, "MONDAY", "11:30", "6/30/17");
        songs.get(0).setData(0,0, "MONDAY", "11:30", "1/15/16");
        songs.get(4).setData(0,0, "MONDAY", "11:30", "3/15/15");
        songs.get(3).setData(0,0, "MONDAY", "11:30", "7/13/11");
        FlashbackPlaylist list = new FlashbackPlaylist(songs, location, "MONDAY","11:30", "2/16/18");
        //ArrayList<Integer> matches = new ArrayList<>(3,6,4,1,2,5);
        ArrayList<Song> sorted = list.sortPlaylist("2/16/18");
        Log.d("Test", Integer.toString(sorted.size()));
        assertEquals("First", sorted.get(0).getTitle());
        assertEquals("1.5", sorted.get(1).getTitle());
        assertEquals("Second", sorted.get(2).getTitle());
        assertEquals("Third", sorted.get(3).getTitle());
        assertEquals("3.5", sorted.get(4).getTitle());
        assertEquals("Fourth", sorted.get(5).getTitle());

    }

}
