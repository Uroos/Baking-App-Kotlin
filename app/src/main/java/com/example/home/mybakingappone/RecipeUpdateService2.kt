package com.example.home.mybakingappone;

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.home.mybakingappone.RecipeUpdateService2.someStrings.ACTION_RECIPE_UPDATE
import com.example.home.mybakingappone.model.Recipes2
import com.google.gson.Gson
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import kotlinx.serialization.stringify
import timber.log.Timber

class RecipeUpdateService2 : IntentService("service") {

    object someStrings {
        @JvmStatic
        val ACTION_RECIPE_UPDATE = "com.example.home.mybakingapp.action.recipe_update";
    }

    companion object {
        fun startRecipeUpdate(context: Context?, ingredients: String, recipe: Recipes2?) {
            var intent = Intent(context, RecipeUpdateService2::class.java)
            var bundle = Bundle()
            bundle.putSerializable(context!!.getString(R.string.widget_key_recipe), recipe)
            intent.setAction(ACTION_RECIPE_UPDATE);
            intent.putExtra(context.getString(R.string.widget_key_ingredients), ingredients)
            intent.putExtras(bundle)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            var action = intent.getAction()
            if (ACTION_RECIPE_UPDATE == action) {
                var ingredients = intent.getStringExtra(getString(R.string.widget_key_ingredients));
                var recipe = intent.getSerializableExtra(getString(R.string.widget_key_recipe)) as Recipes2
                Timber.v("ingredients sent to service are: " + ingredients);
                handleActionRecipeUpdate(ingredients, recipe);
            }
        }
    }

    fun handleActionRecipeUpdate(ingredients: String, recipe: Recipes2) {
        // here we will run data base query to get the favorite recipe and display in widget
        var appWidgetManager = AppWidgetManager.getInstance(this)
        var appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, RecipeWidgetProvider2::class.java))
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_text);
        // Now update the widgets
        RecipeWidgetProvider2.updateRecipeWidgets(this, appWidgetManager, ingredients, recipe, appWidgetIds)
    }
}



