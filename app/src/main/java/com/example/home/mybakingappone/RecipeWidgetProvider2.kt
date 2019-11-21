package com.example.home.mybakingappone

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import android.widget.RemoteViews
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.ui.Main2Activity
import com.example.home.mybakingappone.ui.RecipeDetail2

/**
 * Implementation of App Widget functionality.
 */
class RecipeWidgetProvider2 : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, "", null, appWidgetId);
        }
    }

    companion object {
        fun updateRecipeWidgets(context: Context, appWidgetManager: AppWidgetManager,
                                ingredients: String, recipe: Recipes2, appWidgetIds: IntArray) {

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, ingredients, recipe, appWidgetId);
            }
        }

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, ingredients: String, recipe: Recipes2?, appWidgetId: Int) {
            // Construct the RemoteViews object
            var intent: Intent
            var stackBuilder: TaskStackBuilder
            var pendingIntent: PendingIntent
            val views = RemoteViews(context.packageName, R.layout.recipe_widget_provider)
            if (recipe == null) {
                intent = Intent(context, Main2Activity::class.java)
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            } else { // Set on click to open the corresponding detail activity
                var mainIntent = Intent(context, Main2Activity::class.java)
                stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(Main2Activity::class.java)
                stackBuilder.addNextIntent(mainIntent)
                intent = Intent(context, RecipeDetail2::class.java)
                var bundle = Bundle()
                bundle.putSerializable(context.getString(R.string.main_activity_bundle_recipe), recipe)
                intent.putExtras(bundle)
                // This uses android:parentActivityName and android.support.PARENT_ACTIVITY meta-data by default
                stackBuilder.addNextIntent(intent)
                pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)!!
            }
            //Create an Intent to launch either Main2Activity or RecipeDetailActivity when clicked.
            views.setOnClickPendingIntent(R.id.widget_relative_layout, pendingIntent)
            views.setTextViewText(R.id.recipe_widget_text, ingredients)
            if (recipe == null) {
                views.setTextViewText(R.id.recipe_widget_name, "");
            } else {
                views.setTextViewText(R.id.recipe_widget_name, recipe.name);
            }
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

