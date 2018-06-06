package com.planetpeopleplatform.mybakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MyBakingAppWidget extends AppWidgetProvider {

    // setImageViewResource to update the widget’s image
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String recipe, String recipeIngredients, boolean viewState, int appWidgetId) {


        RemoteViews rv;
        rv = getGardenGridRemoteView(context, viewState, recipe, recipeIngredients);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getGardenGridRemoteView(Context context, boolean viewState, String recipe, String recipeIngredients) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_baking_app_widget);

        if (viewState) views.setViewVisibility(R.id.widget_grid_view, View.GONE);
        else views.setViewVisibility(R.id.widget_grid_view, View.VISIBLE);

        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent appIntent = new Intent(context, RecipeIngridientsWidgetService.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);

        views.setTextViewText(R.id.widget_recipe_tv, recipe);
        views.setTextViewText(R.id.ingredients_widget_tv, recipeIngredients);

        return views;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        RecipeIngridientsWidgetService.startActionUpdateBakingWidgets(context);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           String recipe, String recipeIngredients, boolean viewState,
                                           int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,
                    recipe, recipeIngredients, viewState, appWidgetId);
        }
    }

    @Override
    public void onDeleted (Context context,int[] appWidgetIds){
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled (Context context){
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled (Context context){
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}