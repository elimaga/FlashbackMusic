package com.example.team13.flashbackmusic;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.MimeTypeFilter;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.team13.flashbackmusic.interfaces.DownloadObserver;
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

public class Unzipper extends AsyncTask<String, Integer, Boolean> implements Subject<UnzipperObserver>, DownloadObserver {

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

    @Override
    public void onCompleteDownload(Context context, Uri uri, String mime) {

        if (MimeTypeFilter.matches(mime, new String[]{"audio/mpeg3","audio/x-mpeg-3","video/mpeg","video/x-mpeg"})!= null){
            notifyObservers();
        } else if (MimeTypeFilter.matches(mime,
                new String[]{"application/x-compressed", "application/x-zip-compressed","application/zip","multipart/x-zip"})!= null) {
            String path = uri.getPath();
            String zipname = uri.getLastPathSegment();
            unpackZip(path, zipname);
        } else {
            Toast.makeText(context, "invalid file format", Toast.LENGTH_SHORT).show();
        }

    }
}
