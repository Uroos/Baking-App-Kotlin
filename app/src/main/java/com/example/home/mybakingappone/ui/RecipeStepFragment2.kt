package com.example.home.mybakingappone.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.RecipeUpdateService2
import com.example.home.mybakingappone.model.Ingredients2
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.model.Steps2
import com.example.home.mybakingappone.ui.RecipeStepFragment2.RecipeClass.recipe
import com.squareup.picasso.Picasso
import timber.log.Timber
import android.graphics.Color.parseColor
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.ShapeDrawable
import android.graphics.Color


/**
 * A simple [Fragment] subclass.
 */
class RecipeStepFragment2 : Fragment(), RecipeStepListAdapter2.RecipeStepListAdapterOnClickHandler {

    lateinit var textViewRecipeName: TextView
    lateinit var recyclerViewSteps: RecyclerView
    lateinit var textViewIngredients: TextView
    lateinit var fabLike: FloatingActionButton
    lateinit var fabArrow: FloatingActionButton
    lateinit var imageViewRecipe: ImageView
    lateinit var imageViewArrow: ImageView

    // Define a new interface OnImageClickListener that triggers a callback in the host activity

    private lateinit var callback: OnStepClickListener

    var ingredients: String = ""

    private var steps: List<Steps2> = ArrayList()
    private var ingredientsList: ArrayList<Ingredients2> = ArrayList()
    private var recipeStepListAdapter: RecipeStepListAdapter2? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    object RecipeClass {
        @JvmStatic
        var recipe: Recipes2? = null
    }

    // Method in interface to handle recycler view clicks. Calls onStepSelected implemented in RecipeDetail2
    override fun onStepClick(step: Steps2,position:Int, steps: List<Steps2>) {
        var tempVideoUrl = step.videoURL
        var tempThumbNailUrl = step.thumbnailURL
        var urlToSend: String?
        if (tempVideoUrl == null || TextUtils.isEmpty(tempVideoUrl)) {
            if (tempThumbNailUrl == null || TextUtils.isEmpty(tempThumbNailUrl)) {
                urlToSend = null
            } else {
                urlToSend = tempThumbNailUrl
            }
        } else {
            urlToSend = tempVideoUrl;
        }
        callback.onStepSelected(step.description,step.shortDescription, urlToSend,position,steps);
    }

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    interface OnStepClickListener {
        fun onStepSelected(description: String, shortDescription:String, videoUrl: String?,position:Int, steps: List<Steps2>)
    }

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
        val activity = activity as Context
        // Inflate the RecipeStepFragment layout
        if (savedInstanceState == null) {
            Timber.v("savedinstancestate is null. trying to get stored recipe")
            steps = recipe!!.steps
            ingredientsList = recipe!!.ingredients
        } else {
            recipe = savedInstanceState.getSerializable("recipe") as Recipes2
            steps = recipe!!.steps
            ingredientsList = recipe!!.ingredients
        }
        textViewRecipeName = rootView.findViewById(R.id.tv_recipe_name)
        textViewIngredients = rootView.findViewById(R.id.tv_ingredients)
        imageViewRecipe = rootView.findViewById(R.id.iv_recipe)
        fabLike = rootView.findViewById(R.id.fab_like)
        fabArrow=rootView.findViewById(R.id.fab_arrow)
        recyclerViewSteps = rootView.findViewById(R.id.rv_recipe_step)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewSteps.setLayoutManager(linearLayoutManager)
        recyclerViewSteps.setHasFixedSize(true)
        recipeStepListAdapter = RecipeStepListAdapter2(activity, steps, this)//added !!
        recyclerViewSteps.setAdapter(recipeStepListAdapter)

        textViewRecipeName.setText(recipe!!.name)
        ingredients = arrangeIngredientList(ingredientsList)
        textViewIngredients.setText(ingredients)
        Picasso.with(this.context).load((recipe!!.image).toInt()).error(R.mipmap.ic_launcher).into(imageViewRecipe)

        fabLike.setOnClickListener() {
            RecipeUpdateService2.startRecipeUpdate(context, ingredients, recipe)
            Toast.makeText(this.context,"Your recipe is added to the widget!", Toast.LENGTH_SHORT).show()
        }
        fabArrow.setOnClickListener(){
            getActivity()?.onBackPressed()
        }
        return rootView;
    }

    fun arrangeIngredientList(ingredientsList: ArrayList<Ingredients2>): String {
        var stringBuilder = StringBuilder()
        var result=""
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
        outState.putSerializable("recipe", recipe)
        outState.putString("ingredients", ingredients)
        //outState.putParcelable(BUNDLE_STEPS_RECYCLER_LAYOUT, recyclerViewSteps.getLayoutManager().onSaveInstanceState());
    }

    fun setRecipe(recipe: Recipes2) {
        RecipeClass.recipe = recipe
    }

}

