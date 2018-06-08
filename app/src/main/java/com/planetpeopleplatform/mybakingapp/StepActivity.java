package com.planetpeopleplatform.mybakingapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.planetpeopleplatform.mybakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Hammedopejin on 5/24/2018.
 */

public class StepActivity extends AppCompatActivity {

    private ActionBar mSupportActionBar;

    @BindView(R.id.activity_step_frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.body_container)
    FrameLayout bodyFrameLayout;
    @BindView(R.id.head_container)
    FrameLayout headFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        mSupportActionBar = getSupportActionBar();
        assert mSupportActionBar != null;
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);

        // Only create new fragments when there is no previously saved state
        if(savedInstanceState == null) {

            Bundle extra = getIntent().getBundleExtra("bundle");
            List<Step> mStepArray = extra.getParcelableArrayList(getString(R.string.steps));
            int position = extra.getInt(getString(R.string.position));
            assert mStepArray != null;
            String videoUrl = mStepArray.get(position).getVideoURL();

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Create a new head BodyPartFragment
            HeadFragment headFragment = new HeadFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.head_container, headFragment)
                    .commit();
            headFragment.setVideoUrl(videoUrl);


            // Create and display the body BodyPartFragments
            BodyFragment bodyFragment = new BodyFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit();
                BodyFragment.setStepData(mStepArray, position);


        }

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

    @Override
    protected void onResume() {
        super.onResume();
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
            mSupportActionBar.hide();
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
            bodyFrameLayout.setVisibility(View.GONE);
            headFrameLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
                    Resources.getSystem().getDisplayMetrics().heightPixels));
        }
    }
}
