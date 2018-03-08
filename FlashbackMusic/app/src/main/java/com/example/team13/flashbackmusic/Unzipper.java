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

public class Unzipper extends AsyncTask<String, Integer, Boolean> implements Subject<UnzipperObserver> {

    ArrayList<UnzipperObserver> observers;
    ArrayList<String> paths;

    public enum Result {
        INVALID_FORMAT, SUCCESS, ERROR;

        public static Result fromInteger(int x) {
            switch(x) {
                case 0:
                    return INVALID_FORMAT;
                case 1:
                    return SUCCESS;
                case 2:
                    return ERROR;
            }
            return null;
        }

        public Integer toInt() {
            switch(this) {
                case INVALID_FORMAT:
                    return 0;
                case SUCCESS:
                    return 1;
                case ERROR:
                    return 2;
            }
            return null;
        }
    }

    public Unzipper() {
        this.observers = new ArrayList<>();
    }

    private Boolean unpackZip(String directoryPath, String zipName)
    {
        InputStream is;
        ZipInputStream zis;
        paths = new ArrayList<>();
        try
        {
            String filename;
            is = new FileInputStream(directoryPath+zipName);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(directoryPath + filename);
                    fmd.mkdirs();
                    continue;
                } else {
                    paths.add(directoryPath+filename);
                }
                if (!(new File(directoryPath+filename).exists())) {
                    FileOutputStream fout = new FileOutputStream(directoryPath + filename);

                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }

                    fout.close();
                }
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
    protected Boolean doInBackground(String... path) {
        String directoryPath = path[0];
        String zipName = path[1];
        boolean isSuccessful = unpackZip(directoryPath, zipName);
        if (isSuccessful) {
            File zipFile = new File(directoryPath+zipName);
            zipFile.delete();
        }
        return isSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Intent intent = new Intent();
        intent.putExtra("result", Result.SUCCESS.toInt());
        intent.putExtra("paths", paths);
        notifyObservers(intent);
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
    public void notifyObservers(Intent intent) {
        Result result = Result.fromInteger(intent.getIntExtra("result", Result.ERROR.toInt()));
        if (result == Result.SUCCESS) {
            for (UnzipperObserver observer : observers) {
                observer.onUnzipSuccess(intent.getStringArrayListExtra("paths"));
            }
        } else if (result == Result.ERROR || result == Result.INVALID_FORMAT) {
            for (UnzipperObserver observer : observers) {
                observer.onUnzipFailure();
            }
        }
    }

}
