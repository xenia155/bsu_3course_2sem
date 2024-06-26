package com.sad.guess_number;

import static androidx.test.espresso.action.ViewActions.click;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.sad.guess_number", appContext.getPackageName());
    }

    @Before
    public void launchActivity() {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testGuessNumber() {
        Espresso.onView(ViewMatchers.withId(R.id.editText1)).perform(ViewActions.typeText("100"));
        Espresso.onView(ViewMatchers.withId(R.id.button1)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.editText1)).check(ViewAssertions.matches(ViewMatchers.withText("")));
    }

    @Test
    public void test_performing() {
        Espresso.onView(ViewMatchers.withId(R.id.editText1)).perform(ViewActions.typeText("500"));
        Espresso.onView(ViewMatchers.withId(R.id.button1)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.textView1)).check(ViewAssertions.matches(ViewMatchers.withText("Неверный ввод!")));
    }

}