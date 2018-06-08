package com.planetpeopleplatform.mybakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.planetpeopleplatform.mybakingapp.adapter.IngredientAdapter;
import com.planetpeopleplatform.mybakingapp.model.Ingredient;
import com.planetpeopleplatform.mybakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Hammedopejin on 5/24/2018.
 */

public class MasterFragment extends Fragment implements IngredientAdapter.IngredientAdapterOnClickHandler {

    private IngredientAdapter mIngredientAdapter;
    private int mPosition = -1;
    List<Step> mStepArray;
    List<Ingredient> ingredientArray;

    @BindView(R.id.ingredients_tv)
    public TextView mStepDescriptionTV;

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView mRView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    // Mandatory empty constructor
    public MasterFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        ButterKnife.bind(this, rootView);

        showLoading();

        if (getActivity().getIntent() != null) {
            Bundle extra = getActivity().getIntent().getBundleExtra("bundle");


            ingredientArray = extra.getParcelableArrayList(getString(R.string.ingredients));
            mStepArray = extra.getParcelableArrayList(getString(R.string.steps));
            mPosition = extra.getInt(getString(R.string.position));


        }

        StringBuilder ingredientString = new StringBuilder();
        for (int i = 0; i < ingredientArray.size(); i++){
            if(i < (ingredientArray.size() - 1)) {
                ingredientString.append(ingredientArray.get(i).getIngredient()).append(", ");
            } else {
                ingredientString.append(ingredientArray.get(i).getIngredient());
            }
        }

        getActivity().setTitle(getString(R.string.baking_steps));

        mStepDescriptionTV.setText(ingredientString.toString());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mRView.setLayoutManager(linearLayoutManager);
        mIngredientAdapter = new IngredientAdapter(getActivity(), mStepArray, this,
                (IngredientAdapter.IngredientAdapterOnClickHandler) getActivity());
        mRView.setAdapter(mIngredientAdapter);

        showDataView();
        // Return the root view
        return rootView;
    }

    private void showDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the data is visible */
        mRView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the data */
        mRView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(final int position) {

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                (getString(R.string.my_baking_app)), MODE_PRIVATE);
        final boolean mTablet = sharedPref.getBoolean(getString(R.string.tablet), false);
        if (!mTablet) {
            Intent stepIntent = new Intent(getActivity(), StepActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(getString(R.string.steps),
                    (ArrayList<? extends Parcelable>) mStepArray);
            bundle.putInt(getString(R.string.position), position);

            stepIntent.putExtra("bundle", bundle);

            startActivity(stepIntent);
        }
    }
}

