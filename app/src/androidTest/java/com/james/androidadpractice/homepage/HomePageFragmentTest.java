package com.james.androidadpractice.homepage;

import android.support.test.espresso.contrib.RecyclerViewActions;

import com.james.androidadpractice.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class HomePageFragmentTest {
    @Test
    public void recyclerViewTest() {

        onView(allOf(withId(R.id.recyclerview), isDisplayed()))
                .perform(RecyclerViewActions.scrollToPosition(4));

    }
}