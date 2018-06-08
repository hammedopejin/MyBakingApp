package com.planetpeopleplatform.mybakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.planetpeopleplatform.mybakingapp.adapter.IngredientAdapter;
import com.planetpeopleplatform.mybakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hammedopejin on 5/24/2018.
 */

public class IngredientActivity extends AppCompatActivity implements IngredientAdapter.IngredientAdapterOnClickHandler {

    private List<Step> mStepArray;

    @BindView(R.id.ingredients_tv)
    public TextView mStepDescriptionTV;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                (getString(R.string.my_baking_app)), MODE_PRIVATE);


        if(findViewById(R.id.activity_ingredients_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.tablet), mTwoPane);
            editor.apply();

            Bundle extra = getIntent().getBundleExtra("bundle");
            mStepArray = extra.getParcelableArrayList(getString(R.string.steps));

            assert mStepArray != null;
            String videoUrl = mStepArray.get(0).getVideoURL();

            if(savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Create a new head BodyPartFragment
                HeadFragment headFragment = new HeadFragment();

                fragmentManager.beginTransaction()
                        .add(R.id.head_container, headFragment)
                        .commit();
                headFragment.setVideoUrl(videoUrl);

                // Create and display the body BodyPartFragments
                BodyFragment bodyFragment = new BodyFragment();
                BodyFragment.setStepData(mStepArray, 0);
                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit();

            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public void onClick(int position) {
        if(mTwoPane) {

            String videoUrl = mStepArray.get(position).getVideoURL();

            FragmentManager fragmentManager = getSupportFragmentManager();

            HeadFragment headFragment = new HeadFragment();
            headFragment.setVideoUrl(videoUrl);
            fragmentManager.beginTransaction()
                    .replace(R.id.head_container, headFragment)
                    .commit();



            BodyFragment bodyFragment = new BodyFragment();
            BodyFragment.setStepData(mStepArray, position);
            fragmentManager.beginTransaction()
                    .replace(R.id.body_container, bodyFragment)
                    .commit();
            }
    }
}
