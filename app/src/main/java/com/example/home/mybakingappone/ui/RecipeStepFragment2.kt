package com.example.home.mybakingappone.ui


import android.content.Context
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.RecipeUpdateService
import com.example.home.mybakingappone.model.Ingredients
import com.example.home.mybakingappone.model.Recipes
import com.example.home.mybakingappone.model.Steps
import com.example.home.mybakingappone.ui.RecipeStepFragment2.RecipeClass.recipe
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class RecipeStepFragment2 : Fragment(), RecipeStepListAdapter.RecipeStepListAdapterOnClickHandler {

    //@BindView(R.id.tv_recipe_name)
    lateinit var textViewRecipeName: TextView
    //@BindView(R.id.rv_recipe_step)
    lateinit var recyclerViewSteps: RecyclerView
    //@BindView(R.id.tv_ingredients)
    lateinit var textViewIngredients: TextView
    //@BindView(R.id.fab)
    lateinit var fab: FloatingActionButton
    // Define a new interface OnImageClickListener that triggers a callback in the host activity

    private lateinit var callback: OnStepClickListener

    var ingredients: String = ""

    private var steps: List<Steps> = ArrayList()
    private var ingredientsList: ArrayList<Ingredients> = ArrayList()
    private var recipeStepListAdapter: RecipeStepListAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    object RecipeClass {
        @JvmStatic
        var recipe: Recipes? = null
    }

    // Method in interface to handle recycler view clicks
    override fun onStepClick(step: Steps) {
        var tempVideoUrl = step.getVideoURL()
        var tempThumbNailUrl = step.getThumbnailURL()
        var urlToSend: String? = ""
        if (tempVideoUrl == null || TextUtils.isEmpty(tempVideoUrl)) {
            if (tempThumbNailUrl == null || TextUtils.isEmpty(tempThumbNailUrl)) {
                urlToSend = null
            } else {
                urlToSend = tempThumbNailUrl
            }
        } else {
            urlToSend = tempVideoUrl;
        }
        callback.onStepSelected(step.getDescription(), urlToSend);
    }

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    interface OnStepClickListener {
        fun onStepSelected(description: String, videoUrl: String?)
    }

//    //Empty constructor is necessary for instantiating the fragment
//    fun RecipeStepFragment2() {
//
//    }

    // Override onAttach to make sure that the container activity has implemented the callback
    override fun onAttach(@NonNull context1: Context) {
        super.onAttach(context1)
        if (context1 is OnStepClickListener) {
            callback = context1
        } else {
            throw ClassCastException(
                    context.toString() + " must implement OnDogSelected.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false)
        textViewRecipeName = rootView.findViewById(R.id.tv_recipe_name)
        recyclerViewSteps = rootView.findViewById(R.id.rv_recipe_step)
        textViewIngredients = rootView.findViewById(R.id.tv_ingredients)
        fab = rootView.findViewById(R.id.fab)

        // Inflate the RecipeStepFragment layout
        if (savedInstanceState == null) {
            Timber.v("savedinstancestate is null. trying to get stored recipe")
            steps = recipe!!.getSteps()
            ingredientsList = recipe!!.getIngredients()
        } else {
            recipe = savedInstanceState.getSerializable("recipe") as Recipes
            steps = recipe!!.getSteps()
            ingredientsList = recipe!!.getIngredients()
        }
        textViewRecipeName.setText(recipe!!.getName())
        ingredients = arrangeIngredientList(ingredientsList)
        textViewIngredients.setText(ingredients)

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewSteps.setLayoutManager(linearLayoutManager)
        recyclerViewSteps.setHasFixedSize(true)
        recipeStepListAdapter = RecipeStepListAdapter(context, steps, this)
        recyclerViewSteps.setAdapter(recipeStepListAdapter)

        fab.setOnClickListener() {
            RecipeUpdateService.startRecipeUpdate(context, ingredients, recipe)
        }
        return rootView;
    }

    fun arrangeIngredientList(ingredientsList: ArrayList<Ingredients>): String {
        var stringBuilder = StringBuilder()
        var result = ""
        for (i in 0 until ingredientsList.size) {
            //var quantity = String.valueOf(ingredientsList.get(i).getQuantity());
            var quantity = ingredientsList.get(i).quantity
            var measure = ingredientsList.get(i).measure
            var ingredient = ingredientsList.get(i).ingredient
            stringBuilder.append(quantity)
                    .append(" ")
                    .append(measure)
                    .append("    ")
                    .append(ingredient)
                    .append("\n")
        }
        result = stringBuilder.toString()
        Timber.v("Ingredient list is: " + result)
        return result
    }

    override fun onSaveInstanceState(@NonNull outState: Bundle) {
        super.onSaveInstanceState(outState)
        //This might give problem as initially recipe was serialized to save to outState
        outState.putSerializable("recipe", recipe)
        outState.putString("ingredients", ingredients)
        //outState.putParcelable(BUNDLE_STEPS_RECYCLER_LAYOUT, recyclerViewSteps.getLayoutManager().onSaveInstanceState());
    }

    fun setRecipe(recipe: Recipes) {
        RecipeClass.recipe = recipe
    }

}

