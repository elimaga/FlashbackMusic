package com.example.team13.flashbackmusic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by rolandkong on 2/15/18.
 */

public class UserInfo extends AppCompatActivity{

    static final int REQUEST_LOCATION = 1;
    static final int INVALID_COORDINATE = 200;

    static String time;
    static int dayOfWeek;
    static int mmonth, mday, myear;
    static boolean mockflag = false;
    static Calendar calendar;

    public static void setCalendar(Calendar cal){
        calendar = cal;
    }

    public static void mockTime(int hour, int min){
        mockflag = true;
        String minute;

        // Add extra 0 if int is not a double digit
        if (min < 10) {
            minute = "0" + Integer.toString(min);
        } else {
            minute = Integer.toString(min);
        }

        time =  hour + ":" + minute;
    }

    public static void setRealTime(){
        mockflag = false;
    }

    public static void mockDate(int month, int day, int year){
        mockflag = true;
        mmonth = month + 1;
        mday = day;
        myear = year;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

    }

    public static void setRealDate(){
        mockflag = false;
    }

    public static String getDay()
    {
        int day = 1;
        if (mockflag){
            day = dayOfWeek;
        }
        else {
            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_WEEK);
        }

        switch(day)
        {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }

        return "";
    }

    public static String getTime()
    {
        if (mockflag){
            return time;
        }

        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);//currentTime.getHours();
        int mins = Calendar.getInstance().get(Calendar.MINUTE);//currentTime.getMinutes();
        String minute;

        // Add extra 0 if int is not a double digit
        if (mins < 10) {
            minute = "0" + Integer.toString(mins);
        } else {
            minute = Integer.toString(mins);
        }

        return hours + ":" + minute;
    }

    public static String getDate() {
        if (mockflag){
            return mmonth + "/" + mday + "/" + myear;
        }
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int year = cal.get(Calendar.YEAR);

        return month + "/" + day + "/" + year;
    }

    public static double[] getLocation(Activity activity, LocationManager locationManager)
    {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if  (ActivityCompat.checkSelfPermission ( activity , Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED  || ActivityCompat.checkSelfPermission ( activity ,
                Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null)
            {
                double[] newLocation = {location.getLatitude(), location.getLongitude()};
                Log.d("UserInfo", Double.toString(newLocation[0]) + ", " +
                        Double.toString(newLocation[1]));
                return newLocation;
            }
        }

        double[] newLocation = {INVALID_COORDINATE, INVALID_COORDINATE};
        Log.d("UserInfo", Double.toString(newLocation[0]) + ", " + Double.toString(newLocation[1]));
        return newLocation;

    }

    public static int[] getDateValues(String date) {
        int firstSlash = date.indexOf("/");
        int secondSlash = date.lastIndexOf("/");
        int month = Integer.parseInt(date.substring(0, firstSlash));
        int day = Integer.parseInt(date.substring(firstSlash + 1, secondSlash));
        int year = Integer.parseInt(date.substring(secondSlash + 1, date.length()));

        int[] returnArray = {month, day, year};
        return returnArray;
    }

    public static int[] backOneDay(int month, int day, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        cal.add(Calendar.DAY_OF_YEAR, -1);

        int[] returnArray = {cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal.get(Calendar.YEAR)};
        return returnArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
