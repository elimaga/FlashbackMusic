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

import com.example.team13.flashbackmusic.R;
import com.example.team13.flashbackmusic.SignInActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
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

/**
 * Warning: Test assumes album:
 *          https://d1b10bmlvqabco.cloudfront.net/attach/jc2fhqnhbwl4ii/j85f4pwtei5258/jdhvwzg7m0f5/Take_Yourself_Too_Seriously.zip
 *          is downloaded and a google+ account is already signed in
 */
public class SkipSongTest {

    public static final int TEN_SECONDS_IN_MS = 10000;
    @Rule
    public ActivityTestRule<SignInActivity> mActivityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void skipSongTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction tabView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tab_layout),
                                0),
                        1),
                        isDisplayed()));
        tabView.perform(click());

        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.album_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        relativeLayout.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.titleTextView), withText("Title: Windows Are The Eyes To The House"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Title: Windows Are The Eyes To The House")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.artistTextView), withText("Artist: Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Artist: Forum")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.albumTextView), withText("Album: Take Yourself Too Seriously"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        textView4.check(matches(withText("Album: Take Yourself Too Seriously")));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.imageButton2),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                11),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction textView6 = onView(
                allOf(withId(R.id.titleTextView), withText("Title: Dead Dove Do Not Eat"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("Title: Dead Dove Do Not Eat")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.artistTextView), withText("Artist: Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        textView7.check(matches(withText("Artist: Forum")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.albumTextView), withText("Album: Take Yourself Too Seriously"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        textView8.check(matches(withText("Album: Take Yourself Too Seriously")));

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
