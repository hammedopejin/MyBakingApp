package com.planetpeopleplatform.mybakingapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.planetpeopleplatform.mybakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Hammedopejin on 5/24/2018.
 */

public class StepActivity extends AppCompatActivity {

    private int mPosition;
    private List<Step> mStepArray;
    private FragmentManager fragmentManager;
    private HeadFragment headFragment;
    private BodyFragment bodyFragment;

    @BindView(R.id.activity_step_frame_layout)
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Only create new fragments when there is no previously saved state
        if(savedInstanceState == null) {

            Bundle extra = getIntent().getBundleExtra("bundle");
            mStepArray = extra.getParcelableArrayList(getString(R.string.steps));
            mPosition = extra.getInt(getString(R.string.position));
            String videoUrl = mStepArray.get(mPosition).getVideoURL();

            // Add the fragment to its container using a FragmentManager and a Transaction
            fragmentManager = getSupportFragmentManager();

            // Create a new head BodyPartFragment
            headFragment = new HeadFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.head_container, headFragment)
                    .commit();
            headFragment.setVideoUrl(videoUrl);


            // Create and display the body BodyPartFragments
            bodyFragment = new BodyFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.body_container, bodyFragment)
                    .commit();
            BodyFragment.setStepData(mStepArray, mPosition);

        }

    }

    @SuppressLint("InlinedApi")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects.
            hideSystemUi();
            headFragment.mPlayerView.setLayoutParams(new FrameLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
                    Resources.getSystem().getDisplayMetrics().heightPixels));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //unhide your objects here.
            headFragment.mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            headFragment.mPlayerView.setLayoutParams(new FrameLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
                    Resources.getSystem().getDisplayMetrics().heightPixels/2));
            fragmentManager.beginTransaction().show(bodyFragment).commit();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        headFragment.mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        fragmentManager.beginTransaction().hide(bodyFragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
