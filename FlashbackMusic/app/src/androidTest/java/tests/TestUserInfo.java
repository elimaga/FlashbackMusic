package tests;

import com.example.team13.flashbackmusic.UserInfo;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by rolandkong on 2/17/18.
 */

public class TestUserInfo {

    @Test
    public void testgetDay() {
        Calendar calendar = Calendar.getInstance();
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        String day = "";
        switch(dayofweek)
        {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
        }

        assertEquals(day, UserInfo.getDay());

        /* To manually test,  uncomment block and change first argument to current day.
            Don't forget to recomment after since the day changes with real time!
         */

        //assertEquals("Saturday", UserInfo.getDay());

    }

    @Test
    public void testGetTime(){
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);//currentTime.getHours();
        int mins = Calendar.getInstance().get(Calendar.MINUTE);//currentTime.getMinutes();
        String minute = "";

        // Add extra 0 if int is not a double digit
        if (mins < 10) {
            minute = "0" + Integer.toString(mins);
        } else {
            minute = Integer.toString(mins);
        }

        assertEquals(hours + ":" + minute, UserInfo.getTime());

        /* To manually test,  uncomment block and change first argument to current time.
            Don't forget to recomment after since the time changes with real time!
         */

        //assertEquals("19:17", UserInfo.getTime());

    }

    @Test
    public void testGetDate(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);

        String date = month + "/" + day + "/" + year;

        assertEquals(date, UserInfo.getDate());

        /* To manually test,  uncomment block and change first argument to current date.
            Don't forget to recomment after since the date changes with real time!
         */

        //assertEquals("2/17/2018", UserInfo.getDate());


    }

}
