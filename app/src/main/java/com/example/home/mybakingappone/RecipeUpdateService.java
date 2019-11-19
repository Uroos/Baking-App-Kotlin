//package com.example.home.mybakingappone;
//
//import android.app.IntentService;
//import android.appwidget.AppWidgetManager;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.example.home.mybakingappone.model.Recipes;
//
//import java.util.List;
//
//import timber.log.Timber;
//
//public class RecipeUpdateService extends IntentService {
//
//    public static final String ACTION_RECIPE_UPDATE = "com.example.home.mybakingapp.action.recipe_update";
//
//    public RecipeUpdateService() {
//        super("RecipeUpdateService");
//    }
//
//    public static void startRecipeUpdate(Context context, String ingredients, Recipes recipe) {
//        Intent intent = new Intent(context, RecipeUpdateService.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(context.getString(R.string.widget_key_recipe), recipe);
//        intent.setAction(ACTION_RECIPE_UPDATE);
//        intent.putExtra(context.getString(R.string.widget_key_ingredients), ingredients);
//        intent.putExtras(bundle);
//        context.startService(intent);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_RECIPE_UPDATE.equals(action)) {
//                final String ingredients = intent.getStringExtra(getString(R.string.widget_key_ingredients));
//                final Recipes recipe = (Recipes) intent.getSerializableExtra(getString(R.string.widget_key_recipe));
//                Timber.v("ingredients sent to service are: " + ingredients);
//                handleActionRecipeUpdate(ingredients, recipe);
//            }
//        }
//    }
//
//    private void handleActionRecipeUpdate( String ingredients,Recipes recipe) {
//        // here we will run data base query to get the favorite recipe and display in widget
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
//        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_text);
//        // Now update the widgets
//        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, ingredients, recipe, appWidgetIds);
//    }
//}
