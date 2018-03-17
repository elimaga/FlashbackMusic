package com.example.team13.flashbackmusic;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SortSongsTest {

    public static final int TEN_SECONDS_IN_MS = 10000;
    @Rule
    public ActivityTestRule<SignInActivity> mActivityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void sortSongsTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Sort by title"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.title), withText("Dead Dove Do Not Eat"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Dead Dove Do Not Eat")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.title), withText("Dreamatorium"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        1),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Dreamatorium")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.title), withText("I Just Want To Tell You Both Good Luck"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        2),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("I Just Want To Tell You Both Good Luck")));

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Sort by artist"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.title), withText("Sisters Of The Sun"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Sisters Of The Sun")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.title), withText("Dreamatorium"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        1),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Dreamatorium")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.title), withText("Sky Full Of Ghosts"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        2),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("Sky Full Of Ghosts")));

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.title), withText("Sort by album"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.artist), withText("Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                1),
                        isDisplayed()));
        textView7.check(matches(withText("Forum")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.artist), withText("Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        1),
                                1),
                        isDisplayed()));
        textView8.check(matches(withText("Forum")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.artist), withText("Forum"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        2),
                                1),
                        isDisplayed()));
        textView9.check(matches(withText("Forum")));

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction favoriteStatusImageButton = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                withParent(withId(R.id.song_list_view)),
                                4), hasSibling(withText("I Just Want To Tell You Both Good Luck")),
                        isDisplayed()));
        favoriteStatusImageButton.perform(click());

        ViewInteraction favoriteStatusImageButton2 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                withParent(withId(R.id.song_list_view)),
                                4), hasSibling(withText("Sky Full Of Ghosts")),
                        isDisplayed()));
        favoriteStatusImageButton2.perform(click());

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.title), withText("Favorite"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.title), withText("I Just Want To Tell You Both Good Luck"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                0),
                        isDisplayed()));
        textView10.check(matches(withText("I Just Want To Tell You Both Good Luck")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.title), withText("Sky Full Of Ghosts"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        1),
                                0),
                        isDisplayed()));
        textView11.check(matches(withText("Sky Full Of Ghosts")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.title), withText("Windows Are The Eyes To The House"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        2),
                                0),
                        isDisplayed()));
        textView12.check(matches(withText("Windows Are The Eyes To The House")));

        try {
            Thread.sleep(TEN_SECONDS_IN_MS / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        favoriteStatusImageButton.perform(click());
        favoriteStatusImageButton.perform(click());
        favoriteStatusImageButton2.perform(click());
        favoriteStatusImageButton2.perform(click());
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
