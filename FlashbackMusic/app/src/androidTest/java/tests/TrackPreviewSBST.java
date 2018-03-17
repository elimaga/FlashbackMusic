package tests;


import android.content.Intent;
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
import com.example.team13.flashbackmusic.SignInActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TrackPreviewSBST {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setUp() {
        MainActivity.isEspresso = true;
        mActivityTestRule.launchActivity(new Intent());
    }
    @Test
    public void trackPreviewSBST() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.song_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        relativeLayout.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(10646);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.trackPreviewButton), withText("Tracks"),
                        childAtPosition(
                                allOf(withId(R.id.activity_song),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.popup),
                        childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                0),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction relativeLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.listView),
                                childAtPosition(
                                        withId(R.id.popup),
                                        3)),
                        0),
                        isDisplayed()));
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.close_button), withText("x"),
                        childAtPosition(
                                withId(R.id.popup),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        pressBack();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(10389);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction tabView4 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tab_layout),
                                0),
                        1),
                        isDisplayed()));
        tabView4.perform(click());

        DataInteraction relativeLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.album_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        relativeLayout3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(10648);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.trackPreviewButton), withText("Tracks"),
                        childAtPosition(
                                allOf(withId(R.id.activity_song),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction viewGroup2 = onView(
                allOf(withId(R.id.popup),
                        childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                0),
                        isDisplayed()));
        viewGroup2.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.artist), withText("Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Forum")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.artist), withText("Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        1),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Forum")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.artist), withText("Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listView),
                                        2),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Forum")));

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
