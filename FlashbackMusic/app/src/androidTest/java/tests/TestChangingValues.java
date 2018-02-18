package tests;

import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.R;
import com.example.team13.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by KM on 2/18/2018.
 */

public class TestChangingValues {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp()
    {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        int[] resourceIds = {R.raw._123_go, R.raw.america_religious, R.raw.beautiful_pain,
                R.raw.cant_you_be_mine, R.raw.i_will_not_be_afraid, R.raw.stomp_jump_boogie};
        mainActivity.getActivity().loadLibrary(mediaMetadataRetriever, resourceIds);
    }
    @Test
    public void TestInitialValue(){
        ArrayList<Song> songArrayListTester;
        songArrayListTester = mainActivity.getActivity().getSongs();

        for(int i = 0; i<songArrayListTester.size(); i++){
            Song.FavoriteStatus favoriteStatus = songArrayListTester.get(i).getFavoriteStatus();

            assertEquals(favoriteStatus, Song.FavoriteStatus.NEUTRAL);
        }
    }

    @Test
    public void TestChangingValue() {
        ArrayList<Song> songArrayListTester;
        songArrayListTester = mainActivity.getActivity().getSongs();

        songArrayListTester.get(0).setFavoriteStatus(Song.FavoriteStatus.LIKED);
        songArrayListTester.get(1).setFavoriteStatus(Song.FavoriteStatus.DISLIKED);
        assertEquals(Song.FavoriteStatus.LIKED, songArrayListTester.get(0).getFavoriteStatus());
        assertEquals(Song.FavoriteStatus.DISLIKED, songArrayListTester.get(1).getFavoriteStatus());

        //set the favoriteStatus to equal
        songArrayListTester.get(0).setFavoriteStatus(Song.FavoriteStatus.NEUTRAL);
        songArrayListTester.get(1).setFavoriteStatus(Song.FavoriteStatus.NEUTRAL);
        assertEquals(Song.FavoriteStatus.NEUTRAL, songArrayListTester.get(0).getFavoriteStatus());
        assertEquals(Song.FavoriteStatus.NEUTRAL, songArrayListTester.get(0).getFavoriteStatus());

    }

    @Test
    public void TestChangingValuesBySharedPreference(){

        ArrayList<Song> songArrayListTester;
        songArrayListTester = mainActivity.getActivity().getSongs();
        Song songTester = songArrayListTester.get(0);

        SharedPreferences sharedPreferences = mainActivity.getActivity().getSharedPreferences("flashback", mainActivity.getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(songTester.getTitle() + "_favStatus", "LIKED");
        editor.apply();

        String actualValue = sharedPreferences.getString(songTester.getTitle() + "_favStatus","" );

        assertEquals("LIKED",actualValue);
        assertNotEquals("NEUTRAL",actualValue);
        assertNotEquals("DISLIKED",actualValue);

        editor.putString(songTester.getTitle() + "_favStatus", "NEUTRAL");
        editor.apply();

        actualValue = sharedPreferences.getString(songTester.getTitle() + "_favStatus","" );
        assertEquals("NEUTRAL",actualValue);
    }
}
