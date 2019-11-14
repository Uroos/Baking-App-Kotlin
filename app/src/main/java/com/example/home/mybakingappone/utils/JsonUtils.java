package com.example.home.mybakingappone.utils;

import android.content.Context;

import com.example.home.mybakingappone.model.Ingredients;
import com.example.home.mybakingappone.model.Recipes;
import com.example.home.mybakingappone.model.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class JsonUtils {

    private static int id;
    private static String name;
    private static ArrayList<Ingredients> ingredients;
    private static ArrayList<Steps> steps;
    private static int servings;
    private static String image;

    public static ArrayList<Recipes> parseRecipeJson (Context context, String myResponse) {
        ArrayList<Recipes> recipes= new ArrayList<>();
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        if (myResponse != null) {
            try {
                JSONArray jsonArray = new JSONArray(myResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectOfResult = jsonArray.getJSONObject(i);
                    id=objectOfResult.getInt("id");
                    name = objectOfResult.optString("name","");
                    JSONArray ingredientsArray = objectOfResult.getJSONArray("ingredients");
                    for(int j=0; j<ingredientsArray.length(); j++){
                        JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
                        double quantity = ingredientObject.optDouble("quantity",0);
                        String measure = ingredientObject.optString("measure","");
                        String ingredient = ingredientObject.optString("ingredient","");
                        ingredients.add(new Ingredients(quantity, measure, ingredient));
                    }
                    JSONArray stepsArray = objectOfResult.getJSONArray("steps");
                    for(int k=0; k<stepsArray.length(); k++){
                        JSONObject stepsObject = stepsArray.getJSONObject(k);
                        int id = stepsObject.optInt("id",0);
                        String shortDescription = stepsObject.optString("shortDescription","");
                        String description = stepsObject.optString("description","");
                        String videoUrl = stepsObject.optString("videoURL","");
                        String thumbnailURL=stepsObject.optString("thumbnailURL","");
                        steps.add(new Steps(id, shortDescription, description, videoUrl, thumbnailURL));
                    }
                    servings= objectOfResult.optInt("servings",0);
                    image = objectOfResult.optString("image","");

                    recipes.add(new Recipes(id, name, ingredients, steps, servings, image,false));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return recipes;
        } else {
            return null;
        }
    }
}
