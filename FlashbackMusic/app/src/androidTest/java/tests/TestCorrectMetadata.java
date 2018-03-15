package tests;

import android.media.MediaMetadataRetriever;
import android.support.test.rule.ActivityTestRule;

import com.example.team13.flashbackmusic.Album;
import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.R;
import com.example.team13.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Andrew & Eli on 2/13/18.
 */
/*
public class TestCorrectMetadata {

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


    /**
     * Testing to see if the metadata is loaded correctly for the song and album
     *
    @Test
    public void testLoadingData()
    {
        Song song = mainActivity.getActivity().getSong(0);
        assertEquals("123 Go", song.getTitle());
        assertEquals("Keaton Simons", song.getArtist());
        assertEquals("New & Best of Keaton Simons", song.getAlbumName());
        assertEquals(1, song.getTrack());
        Album album = mainActivity.getActivity().getAlbum(0);
        assertEquals("New & Best of Keaton Simons", album.getAlbumName());
        assertEquals("Keaton Simons", album.getArtist());
    }

    /**
     * Test to make sure new albums are created when a new one is needed.
     * Tests to see if the albums are in the correct order and that the Song objects are in the
     * albums.
     *
    @Test
    public void testAlbumCreation()
    {
        Album album1 = mainActivity.getActivity().getAlbum(0);
        Album album2 = mainActivity.getActivity().getAlbum(1);
        Album album3 = mainActivity.getActivity().getAlbum(2);
        assertEquals("New & Best of Keaton Simons", album1.getAlbumName());
        assertEquals("Keaton Simons", album1.getArtist());
        assertEquals("123 Go", album1.getSongs().get(0).getTitle());
        assertEquals("I Will Not Be Afraid (A Sampler)", album2.getAlbumName());
        assertEquals("Caroline Rose", album2.getArtist());
        assertEquals("America Religious", album2.getSongs().get(4).getTitle());
        assertEquals("Love Is Everywhere", album3.getAlbumName());
        assertEquals("Stacy Jones", album3.getArtist());
        assertEquals("Can't You Be Mine", album3.getSongs().get(5).getTitle());
    }
    /**
     * Test to make sure a new album is not created when a song just needs to be added to an existing
     * album.
     * Tests to see if the song was added to the previously existing album.
     *
    @Test
    public void testAddingToAlbum()
    {
        Album album1 = mainActivity.getActivity().getAlbum(0);
        Album album2 = mainActivity.getActivity().getAlbum(1);
        Album album3 = mainActivity.getActivity().getAlbum(2);
        assertEquals("Beautiful-Pain", album1.getSongs().get(4).getTitle());
        assertEquals("I Will Not Be Afraid", album2.getSongs().get(6).getTitle());
        assertEquals("Stomp Jump Boogie", album3.getSongs().get(3).getTitle());
    }
}
*/
