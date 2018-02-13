package tests;

import android.media.MediaMetadataRetriever;
import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.R;
import com.example.team13.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by andrewyu on 2/13/18.
 */

public class TestCorrectMetadata {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp()
    {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        int[] resourceIds = {R.raw.i_will_not_be_afraid};
        mainActivity.getActivity().loadLibrary(mediaMetadataRetriever, resourceIds);
    }

    @Test
    public void test1()
    {
        Song song = mainActivity.getActivity().getSong(0);
        assertEquals("I Will Not Be Afraid", song.getTitle());
    }
}
