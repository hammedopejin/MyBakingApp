package com.planetpeopleplatform.mybakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MyBakingAppWidget extends AppWidgetProvider {

    // setImageViewResource to update the widget’s image
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String recipe, String recipeIngredients, int appWidgetId) {


        RemoteViews rv;
        rv = getGardenGridRemoteView(context, recipe, recipeIngredients);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getGardenGridRemoteView(Context context, String recipe, String recipeIngredients) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_baking_app_widget);

        views.setTextViewText(R.id.widget_recipe_tv, recipe);
        views.setTextViewText(R.id.ingredients_widget_tv, recipeIngredients);

        return views;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           String recipe, String recipeIngredients,
                                           int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,
                    recipe, recipeIngredients, appWidgetId);
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