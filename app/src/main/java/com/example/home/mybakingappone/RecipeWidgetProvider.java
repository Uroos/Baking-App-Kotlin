package com.example.home.mybakingappone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.home.mybakingappone.model.Recipes;
import com.example.home.mybakingappone.ui.Main2Activity;
import com.example.home.mybakingappone.ui.RecipeDetail2;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String ingredients, Recipes recipe, int appWidgetId) {

        // Construct the RemoteViews object
        Intent intent;
        TaskStackBuilder stackBuilder;
        PendingIntent pendingIntent;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        if (recipe == null) {
            intent = new Intent(context, Main2Activity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else { // Set on click to open the corresponding detail activity
            Intent mainIntent = new Intent(context, Main2Activity.class);
            stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Main2Activity.class);
            stackBuilder.addNextIntent(mainIntent);

            intent = new Intent(context, RecipeDetail2.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(context.getString(R.string.main_activity_bundle_recipe), recipe);
            intent.putExtras(bundle);
            // This uses android:parentActivityName and android.support.PARENT_ACTIVITY meta-data by default
            stackBuilder.addNextIntent(intent);
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        //Create an Intent to launch either Main2Activity or RecipeDetailActivity when clicked.
        views.setOnClickPendingIntent(R.id.widget_relative_layout, pendingIntent);
        views.setTextViewText(R.id.recipe_widget_text, ingredients);
        if (recipe == null) {
            views.setTextViewText(R.id.recipe_widget_name, "");
        } else {
            views.setTextViewText(R.id.recipe_widget_name, recipe.getName());
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

        //RecipeUpdateService.startRecipeUpdate(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, "", null, appWidgetId);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           String ingredients,Recipes recipe, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, ingredients, recipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

