package tests;

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.MusicFileDownloader;
import com.example.team13.flashbackmusic.interfaces.DownloadObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by KM on 3/13/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestDownloading implements DownloadObserver{

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule permissionRule3 = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


    MusicFileDownloader musicFileDownloader;
    MainActivity main;
    boolean downloadDone;
    String mime;
    String filename;
    String directoryPath;


    @Before
    public void setUp(){
        main = mainActivity.getActivity();


    }

    @Test
    public void testDownloading1(){

        String url1 = "https://www.dropbox.com/s/uv93ug0j5r1et6s/DownloadTester.zip?dl=1";
        String urlString = url1.toString();

        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        musicFileDownloader = new MusicFileDownloader(main);
        musicFileDownloader.registerReceiver(this);
        musicFileDownloader.downloadMusicFile(url);


        try {
            Thread.sleep(8500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("DownloadTester.zip",filename);

        File file = new File(directoryPath + filename);
        Log.d("path1",directoryPath + filename);
        assertTrue(file.exists());

        //delete the file and it must return true
        assertTrue(file.delete());
    }

    @Test
    public void testDownloading2(){

        String url1 = "https://www.dropbox.com/s/tu7hxi56r77h3wx/sample.txt?dl=1";
        String urlString = url1.toString();

        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }



        musicFileDownloader = new MusicFileDownloader(main);
        musicFileDownloader.registerReceiver(this);
        musicFileDownloader.downloadMusicFile(url);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("sample.txt",filename);
        assertNotEquals("application/zip",mime);

        File file = new File(directoryPath + filename);
        Log.d("path2",directoryPath + filename);
        assertTrue(file.exists());

        //delete the file and it must return true
        assertTrue(file.delete());

        // test with nonexists file
        File emptyFile = new File (directoryPath + "empty.mp3");
        assertFalse(emptyFile.exists());
    }

    @After
    public void afterTest(){
        musicFileDownloader.unregisterReceiver(this);
    }

    @Override
    public void onCompleteDownload(Context context, Intent intent) {
        mime = intent.getStringExtra("mime");
        filename = intent.getStringExtra("filename");
        directoryPath = intent.getStringExtra("directoryPath");

    }
}
