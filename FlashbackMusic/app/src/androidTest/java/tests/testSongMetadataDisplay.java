package tests;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class testSongMetadataDisplay {

    final double TEST_LATITUDE = 32.881172957516185;
    final double TEST_LONGITUDE = -117.2374677658081;
    final String TEST_LOCATION = "Torrey Pines";
    final String TEST_DAY = "Sunday";
    final String TEST_TIME = "21:00";

    final double DEFAULT_LATITUDE = 200.0;
    final double DEFAULT_LONGITUDE = 200.0;
    final String DEFAULT_LOCATION = "";
    final String DEFAULT_DAY = "";
    final String DEFAULT_TIME = "";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup()
    {
        mActivityTestRule.getActivity().getSongs().get(0).setData(
                DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_DAY, DEFAULT_TIME);
    }

    @Test
    public void testSongMetadataDisplay() {
        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(ViewMatchers.withId(R.id.song_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        relativeLayout.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.titleTextView), withText("Title: 123 Go"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Title: 123 Go")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.artistTextView), withText("Artist: Keaton Simons"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Artist: Keaton Simons")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.albumTextView), withText("Album: New & Best of Keaton Simons"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Album: New & Best of Keaton Simons")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.locationTextView), withText("Location:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        textView4.check(matches(withText("Location:")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.dayTextView), withText("Day: "),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        textView5.check(matches(withText("Day: ")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.timeTextView), withText("Time: "),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        textView6.check(matches(withText("Time: ")));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.BackButton), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton.perform(click());

        //change location, day and time of song metadata
        mActivityTestRule.getActivity().getSongs().get(0).setData(
                TEST_LATITUDE, TEST_LONGITUDE, TEST_DAY, TEST_TIME);

        DataInteraction relativeLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.song_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        relativeLayout2.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.locationTextView), withText("Location: " + TEST_LOCATION),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        textView7.check(matches(withText("Location: " + TEST_LOCATION)));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.dayTextView), withText("Day: " + TEST_DAY),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        textView8.check(matches(withText("Day: " + TEST_DAY)));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.timeTextView), withText("Time: " + TEST_TIME),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        textView9.check(matches(withText("Time: " + TEST_TIME)));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.BackButton), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton2.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
