package com.planetpeopleplatform.mybakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityScreenTest {

    public static final String RECIPE_NAME = "Brownies";

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    /**
     * Clicks on a RecyclerView item and checks it opens up the IngredientActivity with the correct details.
     */
    @Test
    public void clickRecycleViewItem_IngredientsActivity() {

        //Checking that second item retrieved correctly
        onView(withRecyclerView(R.id.recipes_recycler_view).atPositionOnView(1, R.id.name)).check(matches(withText(RECIPE_NAME)));


        onView(withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.ingredients_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredient_recycler_view)).check(matches(isDisplayed()));


        onView(withId(R.id.ingredient_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.body_part_textview)).check(matches(isDisplayed()));

    }

    public static RecyclerMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerMatcher(recyclerViewId);
    }


}