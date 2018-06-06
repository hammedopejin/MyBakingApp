package com.planetpeopleplatform.mybakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planetpeopleplatform.mybakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;

/**
 * Created by Hammedopejin on 5/24/2018.
 */

public class BodyFragment extends Fragment {

    private static List<Step> mStepArray;
    private static int mPosition;


    @BindView(R.id.currentStep)
    TextView mCurrentStep;
    @BindView(R.id.body_part_textview)
    TextView mTextView;
    @BindView(R.id.next_button)
    FloatingActionButton nextBotton;
    @BindView(R.id.back_button)
    FloatingActionButton backBotton;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public BodyFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_body, container, false);
        ButterKnife.bind(this, rootView);


        String mDescText = mStepArray.get(mPosition).getDescription();
        mTextView.setText(mDescText);
        mCurrentStep.setText(new StringBuilder().append(String.valueOf(mPosition + 1)).append(" of ").append(mStepArray.size()).toString());

        nextBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ++mPosition;
                final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                        (getString(R.string.my_baking_app)), MODE_PRIVATE);
                final boolean mTablet = sharedPref.getBoolean(getString(R.string.tablet), false);
                if (!mTablet) {
                    Intent stepIntent = new Intent(getActivity(), StepActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(getString(R.string.steps),
                            (ArrayList<? extends Parcelable>) mStepArray);
                    bundle.putInt(getString(R.string.position), mPosition);

                    stepIntent.putExtra("bundle", bundle);
                    stepIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(stepIntent);
                }

            }
        });

        backBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                --mPosition;

                    Intent stepIntent = new Intent(getActivity(), StepActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("steps",
                            (ArrayList<? extends Parcelable>) mStepArray);
                    bundle.putInt("position", mPosition);

                    stepIntent.putExtra("bundle", bundle);
                    stepIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(stepIntent);


            }
        });

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                ("mybakingapp"), MODE_PRIVATE);
        final boolean mTablet = sharedPref.getBoolean("tablet", false);
        if (mTablet) {
            mTextView.setTextSize(24.0f);
            nextBotton.setVisibility(View.GONE);
            backBotton.setVisibility(View.GONE);
            mCurrentStep.setVisibility(GONE);
        }
        if (mPosition >= (mStepArray.size() - 1)){
            nextBotton.setVisibility(View.GONE);
        }
        if (mPosition <= 0){
            backBotton.setVisibility(View.GONE);
        }
        return rootView;
    }


    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {

    }

    public static void setStepData(List<Step> stepArray, int position){
        mStepArray = stepArray;
        mPosition = position;
    }
}
