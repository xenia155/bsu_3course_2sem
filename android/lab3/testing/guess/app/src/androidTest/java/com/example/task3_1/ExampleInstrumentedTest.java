package com.example.task3_1;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Before
    public void setUp() throws Exception {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testInputField_DisplayCorrectInput() {
        String input = "123";
        onView(withId(R.id.editTextNumber)).perform(typeText(input), closeSoftKeyboard());
        onView(withId(R.id.editTextNumber)).check(matches(withText(input)));
    }

    @Test
    public void testInputField_OnlyAcceptsNumbers() {
        String input = "abc";
        onView(withId(R.id.editTextNumber)).perform(typeText(input), closeSoftKeyboard());
        onView(withId(R.id.editTextNumber)).check(matches(withText("")));
    }

    @Test
    public void testImage_Displayed() {
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }
}
