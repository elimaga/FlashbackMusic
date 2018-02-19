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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestChangingSymbol {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testChangeSymbol() {
        ViewInteraction favoriteStatusImageButton = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                withParent(withId(R.id.song_list_view)),
                                4),hasSibling(withText("123 Go")),
                        isDisplayed()));
        favoriteStatusImageButton.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                4), hasSibling(withText("123 Go")),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction favoriteStatusImageButton2 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                withParent(withId(R.id.song_list_view)),
                                4), hasSibling(withText("123 Go")),
                        isDisplayed()));
        favoriteStatusImageButton2.perform(click());

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                4), hasSibling(withText("123 Go")),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction favoriteStatusImageButton3 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                withParent(withId(R.id.song_list_view)),
                                4), hasSibling(withText("123 Go")),
                        isDisplayed()));
        favoriteStatusImageButton3.perform(click());

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        0),
                                4), hasSibling(withText("123 Go")),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction favoriteStatusImageButton4 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                withParent(withId(R.id.song_list_view)),
                                4), hasSibling(withText("Back East")),
                        isDisplayed()));
        favoriteStatusImageButton4.perform(click());

        ViewInteraction favoriteStatusImageButton5 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                withParent(withId(R.id.song_list_view)),
                                4), hasSibling(withText("Beautiful-Pain")),
                        isDisplayed()));
        favoriteStatusImageButton5.perform(click());

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        3),
                                4), hasSibling(withText("Back East")),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction imageButton5 = onView(
                allOf(withId(R.id.favoriteSymbol),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.song_list_view),
                                        4),
                                4), hasSibling(withText("Beautiful-Pain")),
                        isDisplayed()));
        imageButton5.check(matches(isDisplayed()));

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
