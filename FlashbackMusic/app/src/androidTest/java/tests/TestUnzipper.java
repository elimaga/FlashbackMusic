package tests;

import android.Manifest;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;
import android.util.Log;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.R;
import com.example.team13.flashbackmusic.Unzipper;
import com.example.team13.flashbackmusic.interfaces.UnzipperObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Kazutaka on 3/13/18.
 */

@RunWith(AndroidJUnit4.class)
public class TestUnzipper {


    class TestUnzipperObserver implements UnzipperObserver{

        @Override
        public void onUnzipSuccess(ArrayList<String> paths) {

        }

        @Override
        public void onUnzipFailure() {
        }
    }


    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRuleWrite = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule permissionRuleRead = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Before
    public void setup() {
        ArrayList<Integer> resIds = new ArrayList<>();
        resIds.add(R.raw.windows_zipped);
        copier(resIds);
        Unzipper unzipper = new Unzipper();
        String directory = "/storage/emulated/0/";
        String filename = Integer.toString(R.raw.windows_zipped);
        TestUnzipperObserver observer = new TestUnzipperObserver();
        unzipper.registerObserver(observer);
        unzipper.execute(directory, filename);
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testunpackZipWithWindowsZip() {
        String directory = "/storage/emulated/0/";
        String filename = "hello_world.txt";
        File file = new File(directory+filename);
        assertTrue(file.exists());
        if(file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testunpackZipWithMacZip() {
        String directory = "/storage/emulated/0/";
        String filename = "hello_world.txt";
        File file = new File(directory+filename);
        assertTrue(file.exists());
        if(file.exists()) {
            file.delete();
        }
    }



    private void copier(ArrayList<Integer> resIds) {

        for(int resId: resIds) {
            String filename = Integer.toString(resId);
            InputStream in;
            OutputStream out;
            try {
                String outDir = Environment.getExternalStorageDirectory().getAbsolutePath();

                File outFile = new File(outDir, filename);
                out = new FileOutputStream(outFile);
                in = mainActivity.getActivity().getResources().openRawResource(R.raw.windows_zipped);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e("tag", "Failed to copy resource file: " + filename, e);
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}
