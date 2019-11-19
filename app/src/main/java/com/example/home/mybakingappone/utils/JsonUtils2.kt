package com.example.home.mybakingappone.utils

//import com.example.home.mybakingappone.model.Recipes
import android.content.Context
import com.example.home.mybakingappone.model.Ingredients2
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.model.Steps2
import org.json.JSONArray
import org.json.JSONException

fun parseRecipeJson(context: Context, myResponse: String): ArrayList<Recipes2>? {
    var recipes = ArrayList<Recipes2>()
    var ingredients = ArrayList<Ingredients2>()
    var steps = ArrayList<Steps2>()
    var id = 0
    var name = ""
    var servings = 0
    var image = ""


    if (myResponse != null) {
        try {
            var jsonArray = JSONArray(myResponse);
            for (i in 0 until jsonArray.length()) {
                var objectOfResult = jsonArray.getJSONObject(i)
                id = objectOfResult.getInt("id")
                name = objectOfResult.optString("name", "")
                var ingredientsArray = objectOfResult.getJSONArray("ingredients")

                for (j in 0 until ingredientsArray.length()) {
                    var ingredientObject = ingredientsArray.getJSONObject(j)
                    var quantity = ingredientObject.optDouble("quantity", 0.0)
                    var measure = ingredientObject.optString("measure", "")
                    var ingredient = ingredientObject.optString("ingredient", "")
                    ingredients.add(Ingredients2(quantity, measure, ingredient))
                }
                var stepsArray = objectOfResult.getJSONArray("steps")
                for (k in 0 until stepsArray.length()) {
                    var stepsObject = stepsArray.getJSONObject(k)
                    var id = stepsObject.optInt("id", 0)
                    var shortDescription = stepsObject.optString("shortDescription", "")
                    var description = stepsObject.optString("description", "")
                    var videoUrl = stepsObject.optString("videoURL", "")
                    var thumbnailURL = stepsObject.optString("thumbnailURL", "")
                    steps.add(Steps2(id, shortDescription, description, videoUrl, thumbnailURL))
                }
                servings = objectOfResult.optInt("servings", 0)
                image = objectOfResult.optString("image", "")

                recipes.add(Recipes2(id, name, ingredients, steps, servings, image))

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return recipes
    } else {
        return null
    }
}