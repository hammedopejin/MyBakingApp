package com.planetpeopleplatform.mybakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.planetpeopleplatform.mybakingapp.data.BakingContract;

import static com.planetpeopleplatform.mybakingapp.data.BakingContract.BASE_CONTENT_URI;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;
    private Cursor mCursor2;

    GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        Uri RECIPE_URI = BASE_CONTENT_URI.buildUpon().appendPath(BakingContract.RECIPE_PATH).build();
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                RECIPE_URI,
                null,
                null,
                null,
                null
        );

        Uri INGREDIENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(BakingContract.INGREDIENT_PATH).build();

        if (mCursor2 != null) mCursor2.close();
        mCursor2 = mContext.getContentResolver().query(
                INGREDIENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        mCursor.close();
        mCursor2.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);
        String recipeName = mCursor.getString(mCursor.getColumnIndex(BakingContract.RecipesEntry.COLUMN_RECIPE_NAME));




        StringBuilder recipeIngredients = new StringBuilder();
        assert mCursor2 != null;
        mCursor2.moveToPosition(position);
        for (int i = 1; i < mCursor2.getCount()/2; i++) {

            recipeIngredients.append(mCursor2.getString(mCursor2.getColumnIndex(BakingContract.IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT)));

            if (!mCursor2.isLast()) {
                mCursor2.moveToNext();
            }
        }


        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget);
        int imgRes;
        switch (position){
            case 0:
                imgRes = R.drawable.nutella_pie;
                break;

            case 1:
                imgRes = R.drawable.brownies;
                break;
            case 2:
                imgRes = R.drawable.yellow_cake;
                break;
            case 3:
                imgRes = R.drawable.cheesecake;
                break;
                default:
                    imgRes = R.drawable.nutella_pie;
                    break;
        }

        views.setImageViewResource(R.id.widget_recipe_image, imgRes);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.setTextViewText(R.id.ingredients_widget_tv, recipeIngredients.toString());


        Bundle extras = new Bundle();
        extras.putLong(RecipeIngridientsWidgetService.EXTRA_RECIPE_ID, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_recipe_image, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
