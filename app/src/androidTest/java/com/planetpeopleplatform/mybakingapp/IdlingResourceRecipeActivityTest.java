package com.planetpeopleplatform.mybakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class IdlingResourceRecipeActivityTest {

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested, RecipeActivity in this case, will be launched
     * before each test that's annotated with @Test and before methods annotated with @Before.
     *
     * The activity will be terminated after the test and methods annotated with @After are
     * complete. This rule allows you to directly access the activity during the test.
     */
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource mIdlingResource;


    // Registers any resource that needs to be synchronized with Espresso before
    // the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getmSimpleIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    //Test that the recyclerView with Recipes appears and we can click an item item
    @Test
    public void idlingResourceTest() {

        //Make click on item , at example at position 1
        onView(ViewMatchers.withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

    //Unregister resources when not needed to avoid malfunction
    @After
    public void unregisterIdlingResource() {
        if(mIdlingResource != null){
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}