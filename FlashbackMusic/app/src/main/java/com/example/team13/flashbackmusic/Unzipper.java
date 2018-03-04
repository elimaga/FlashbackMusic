package com.example.team13.flashbackmusic;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.team13.flashbackmusic.interfaces.Subject;
import com.example.team13.flashbackmusic.interfaces.UnzipperObserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Kazutaka on 3/3/18.
 */

public class Unzipper extends AsyncTask<String, Integer, Boolean> implements Subject<UnzipperObserver> {

    ArrayList<UnzipperObserver> observers;

    private Boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // doInBackground(String path, String zipname)
    @Override
    protected Boolean doInBackground(String... pathAndZipname) {
        return unpackZip(pathAndZipname[0], pathAndZipname[1]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        notifyObservers();

    }

    @Override
    public void registerObserver(UnzipperObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(UnzipperObserver observer) {
        int index = observers.indexOf(observer);
        if (index > 0) observers.remove(index);
    }

    @Override
    public void notifyObservers() {
        for (UnzipperObserver observer : observers) {
            observer.onUnzip();
        }
    }
}
