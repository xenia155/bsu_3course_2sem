package com.mypackage.guessthenumber;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<Game> activityScenarioRule = new ActivityScenarioRule<>(Game.class);

    @Test
    public void checkText() {
        onView(withId(R.id.textView)).check(matches(withText(R.string.try_to_guess)));
    }

    @Test
    public void checkInvalidInput() {
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText(R.string.error)));
    }

    @Test
    public void checkProgressBar() {
        onView(withId(R.id.progressBar2)).check(matches(isDisplayed()));
    }
}