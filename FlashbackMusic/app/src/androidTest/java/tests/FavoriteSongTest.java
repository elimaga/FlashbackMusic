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
import android.widget.Button;

import com.example.team13.flashbackmusic.MainActivity;
import com.example.team13.flashbackmusic.MusicLibrary;
import com.example.team13.flashbackmusic.R;
import com.example.team13.flashbackmusic.Song;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

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
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavoriteSongTest {

    public static final int TEN_SECONDS_IN_MS = 10000;
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setUp() {
        MainActivity.isEspresso = true;
        mActivityTestRule.launchActivity(new Intent());
    }
    @Test
    public void favoriteSongTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        ViewInteraction oj = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        oj.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3594032);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.appBar),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3587004);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(longClick());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(longClick());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction linearLayout = onView(
                allOf(withContentDescription("Paste"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        1),
                                0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("https://d1b10bmlvqabco.cloudfront.net/attach/jc2fhqnhbwl4ii/j85f4pwtei5258/jdhvwzg7m0f5/Take_Yourself_Too_Seriously.zip"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.downloadButton), withText("DOWNLOAD"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.imageButton),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3473929);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        ViewInteraction tabView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                ViewMatchers.withId(R.id.tab_layout),
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
            Thread.sleep(TEN_SECONDS_IN_MS / 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView1 = onView(
                allOf(withId(R.id.titleTextView), withText("Title: Windows Are The Eyes To The House"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        textView1.check(matches(withText("Title: Windows Are The Eyes To The House")));

        ViewInteraction favoriteStatusImageButton = onView(
                allOf(withId(R.id.favoriteButton),

                        isDisplayed()));
        favoriteStatusImageButton.perform(click());

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.favoriteButton),

                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.imageButton2),

                        isDisplayed()));
        appCompatImageButton3.perform(click());

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.titleTextView), withText("Title: Dead Dove Do Not Eat"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Title: Dead Dove Do Not Eat")));

        ViewInteraction favoriteStatusImageButton2 = onView(
                allOf(withId(R.id.favoriteButton),

                        isDisplayed()));
        favoriteStatusImageButton2.perform(click());

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction favoriteStatusImageButton3 = onView(
                allOf(withId(R.id.favoriteButton),

                        isDisplayed()));
        favoriteStatusImageButton3.perform(click());

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.favoriteButton),

                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.titleTextView), withText("Title: Sisters Of The Sun"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Title: Sisters Of The Sun")));

        pressBack();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction tabView2 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tab_layout),
                                0),
                        0),
                        isDisplayed()));
        tabView2.perform(click());

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                4),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        1),
                                4),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        assertEquals(true, checkFavoriteStatus());
        //reset song favorite status for other tests
        imageButton3.perform(click());
        imageButton3.perform(click());
        imageButton4.perform(click());
    }

    private boolean checkFavoriteStatus() {

        boolean favorited = false;
        boolean disliked = false;

        MusicLibrary library = MusicLibrary.getInstance(mActivityTestRule.getActivity());
        ArrayList<Song> songs = library.getSongs();
        for(Song song : songs) {
            if(song.getTitle().equals("Windows Are The Eyes To The House")) {
                if(song.getFavoriteStatus() == Song.FavoriteStatus.LIKED) {
                    favorited = true;
                }
            }
            else if(song.getTitle().equals("Dead Dove Do Not Eat")) {
                if(song.getFavoriteStatus() == Song.FavoriteStatus.DISLIKED) {
                    disliked = true;
                }
            }
        }

        return favorited && disliked;
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
