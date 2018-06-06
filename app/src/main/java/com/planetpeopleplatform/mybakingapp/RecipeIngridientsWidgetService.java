package com.planetpeopleplatform.mybakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.planetpeopleplatform.mybakingapp.data.BakingContract;


public class RecipeIngridientsWidgetService extends IntentService {

    public static final String EXTRA_RECIPE_ID = "com.planetpeopleplatform.mybakingapp.extra.RECIPE_ID";
    public static final Long INVALID_PLANT_ID = 0l;

    boolean mViewState = true;
    Cursor mCursor;

    public RecipeIngridientsWidgetService() {
        super("RecipeIngridientsWidgetService");
    }

    public static void startActionUpdateBakingWidgets(Context context) {
        Intent intent = new Intent(context, RecipeIngridientsWidgetService.class);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mViewState = false;
            final long recipeId = intent.getLongExtra(RecipeIngridientsWidgetService.EXTRA_RECIPE_ID,
                    INVALID_PLANT_ID);
            if (recipeId == 0) {
                handleActionUpdateBakingWidgets("1", "Nutella");
            } else if (recipeId == 1) {
                handleActionUpdateBakingWidgets("2", "Brownies");
            } else if (recipeId == 2) {
                handleActionUpdateBakingWidgets("3", "Yellow Cake");
            } else if (recipeId == 3) {
                handleActionUpdateBakingWidgets("4", "Cheesecake");
            } else {
                handleActionUpdateBakingWidgets("4", "Cheesecake");
            }
        }

    }


    /**
     * Handle action handleActionUpdateBakingWidgets in the provided background thread
     */
    private void handleActionUpdateBakingWidgets(String id, String recipe) {

        Uri INGREDIENT_URI = BakingContract.IngredientsEntry.buildBakingUriWithIngredientId(id);

        if (mCursor != null) mCursor.close();
        mCursor = getContentResolver().query(
                INGREDIENT_URI,
                null,
                null,
                null,
                null
        );

        boolean cursorHasValidData = false;
        if (mCursor != null && mCursor.moveToFirst()) {

            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        mCursor.moveToFirst();
        String recipeIngredients = "";


        for (int i = 0; i < mCursor.getCount(); i++) {

            recipeIngredients += mCursor.getString(mCursor.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT));

            mCursor.moveToNext();
        }



        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MyBakingAppWidget.class));

        //Now update all widgets
        MyBakingAppWidget.updateRecipeWidgets(this, appWidgetManager, recipe, recipeIngredients , mViewState, appWidgetIds);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

}

