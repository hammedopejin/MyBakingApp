package com.planetpeopleplatform.mybakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.planetpeopleplatform.mybakingapp.adapter.RecipeAdapter;
import com.planetpeopleplatform.mybakingapp.data.BakingContract;
import com.planetpeopleplatform.mybakingapp.idlingResource.BakingIdlingResource;
import com.planetpeopleplatform.mybakingapp.model.Recipe;
import com.planetpeopleplatform.mybakingapp.retrofit.util.Client;
import com.planetpeopleplatform.mybakingapp.retrofit.util.RecipeApiEndpointInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hammedopejin on 5/24/2018.
 */

public class RecipeActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeAdapterOnClickHandler {

    private RecipeAdapter mRecipeAdapter;
    private List<Recipe> mRecipes;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecipeRecyclerView;
    private int mPosition = -1;
    private Uri mUri;

    @Nullable
    private BakingIdlingResource mBakingIdlingResource;


    @VisibleForTesting
    @Nullable
    public IdlingResource getmSimpleIdlingResource(){
        if(null == mBakingIdlingResource){
            mBakingIdlingResource = new BakingIdlingResource();
        }
        return mBakingIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);
        getmSimpleIdlingResource();

        showLoading();

        if ( getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            //tabletView
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(mRecipeRecyclerView.getContext(), 3));
        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecipeRecyclerView.setLayoutManager(linearLayoutManager);
        }
        try {

            Client client = new Client();
            RecipeApiEndpointInterface apiService = Client.getClient().create( RecipeApiEndpointInterface.class );
            Call<List<Recipe>> call = apiService.getRecipes();
            if (mBakingIdlingResource != null) {
                mBakingIdlingResource.setIdleState(false);
            }
            call.enqueue(new Callback<List<Recipe>>() {

                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                    mRecipes = response.body();
                    insertData();

                    mRecipeAdapter = new RecipeAdapter(getApplicationContext(), mRecipes,  RecipeActivity.this);
                    mRecipeRecyclerView.setAdapter(mRecipeAdapter);

                    showDataView();

                    if (mBakingIdlingResource != null) {
                        mBakingIdlingResource.setIdleState(true);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
                    Toast.makeText(RecipeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG", t.getMessage() );

                }
            });
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the data is visible */
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the data */
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    // insert data into database
    public void insertData(){
        Cursor cursor;
        Uri RECIPE_URI = BakingContract.RecipesEntry.CONTENT_URI;

        cursor = getContentResolver().query(
                RECIPE_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToNext()){
            cursor.close();
            return;
        }

        ContentValues[] contentValues = new ContentValues[mRecipes.size()];


        for (int i = 0; i < mRecipes.size(); i++) {

            ContentValues cv = new ContentValues();

            cv.put(BakingContract.RecipesEntry.COLUMN_RECIPE_ID, mRecipes.get(i).getId());
            cv.put(BakingContract.RecipesEntry.COLUMN_RECIPE_NAME, mRecipes.get(i).getName());
            cv.put(BakingContract.RecipesEntry.COLUMN_RECIPE_SERVING, mRecipes.get(i).getServings());
            cv.put(BakingContract.RecipesEntry.COLUMN_RECIPE_IMAGE, mRecipes.get(i).getImage());
            contentValues[i] = cv;
        }
        getApplicationContext().getContentResolver().bulkInsert(BakingContract.RecipesEntry.CONTENT_URI,
                contentValues);

        for (int i = 0; i < mRecipes.size(); i++) {

            StringBuilder ingredientString = new StringBuilder();
            StringBuilder measureString = new StringBuilder();
            StringBuilder quantityString = new StringBuilder();
            ContentValues cv = new ContentValues();
            cv.put(BakingContract.IngredientsEntry.COLUMN_INGREDIENT_ID, mRecipes.get(i).getId());
            for (int j = 0; j < mRecipes.get(i).getIngredients().size(); j++) {

                ingredientString.append(mRecipes.get(i).getIngredients().get(j).getIngredient()).append(", \n");
                measureString.append(mRecipes.get(i).getIngredients().get(j).getMeasure()).append(", \n");
                quantityString.append(mRecipes.get(i).getIngredients().get(j).getQuantity()).append(", \n");

            }
            cv.put(BakingContract.IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT, ingredientString.toString());
            cv.put(BakingContract.IngredientsEntry.COLUMN_INGREDIENT_MEASURE, measureString.toString());
            cv.put(BakingContract.IngredientsEntry.COLUMN_INGREDIENT_QUANTITY, quantityString.toString());

            contentValues[i] = cv;
        }
        getApplicationContext().getContentResolver().bulkInsert(BakingContract.IngredientsEntry.CONTENT_URI,
                contentValues);

        for (int i = 0; i < mRecipes.size(); i++) {

            StringBuilder descriptionString = new StringBuilder();
            StringBuilder shortDescriptionString = new StringBuilder();
            StringBuilder thumbnailUrlString = new StringBuilder();
            StringBuilder videoUrlString = new StringBuilder();
            ContentValues cv = new ContentValues();
            cv.put(BakingContract.StepsEntry.COLUMN_STEP_ID, mRecipes.get(i).getId());
            for (int j = 0; j < mRecipes.get(i).getSteps().size(); j++) {

                descriptionString.append(mRecipes.get(i).getSteps().get(j).getDescription()).append(", \n");
                shortDescriptionString.append(mRecipes.get(i).getSteps().get(j).getShortDescription()).append(", \n");
                thumbnailUrlString.append(mRecipes.get(i).getSteps().get(j).getThumbnailURL()).append(", \n");
                videoUrlString.append(mRecipes.get(i).getSteps().get(j).getVideoURL()).append(", \n");

            }
            cv.put(BakingContract.StepsEntry.COLUMN_STEP_DESCRIPTION, descriptionString.toString());
            cv.put(BakingContract.StepsEntry.COLUMN_STEP_SHORT_DESCRIPTION, shortDescriptionString.toString());
            cv.put(BakingContract.StepsEntry.COLUMN_STEP_THUMBNAIL_URL, thumbnailUrlString.toString());
            cv.put(BakingContract.StepsEntry.COLUMN_STEP_VIDEO_URL, videoUrlString.toString());
            contentValues[i] = cv;
        }
        getApplicationContext().getContentResolver().bulkInsert(BakingContract.StepsEntry.CONTENT_URI,
                contentValues);
    }

    // delete data from database
    public void deleteData(){
        mUri = BakingContract.RecipesEntry.buildBakingUriWithRecipeId(String.valueOf(mRecipes.get(mPosition).getId()));
        getApplicationContext().getContentResolver().delete(mUri,
                null, null);

        mUri = BakingContract.IngredientsEntry.buildBakingUriWithIngredientId(String.valueOf(mRecipes.get(mPosition).getId()));
        getApplicationContext().getContentResolver().delete(mUri,
                null, null);

        mUri = BakingContract.StepsEntry.buildBakingUriWithStepId(String.valueOf(mRecipes.get(mPosition).getId()));
        getApplicationContext().getContentResolver().delete(mUri,
                null, null);
    }

    @Override
    public void onClick(int position) {

        Intent ingredientIntent = new Intent(RecipeActivity.this, IngredientActivity.class);

        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(getString(R.string.ingredients),
                            (ArrayList<? extends Parcelable>) mRecipes.get(position).getIngredients());
        bundle.putParcelableArrayList(getString(R.string.steps),
                (ArrayList<? extends Parcelable>) mRecipes.get(position).getSteps());
        bundle.putInt(getString(R.string.position), position);

        ingredientIntent.putExtra("bundle", bundle);

        startActivity(ingredientIntent);
    }
}
