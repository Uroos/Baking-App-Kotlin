package com.example.home.mybakingappone.ui

import android.app.Activity
import android.app.FragmentManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Recipes
import timber.log.Timber

class RecipeDetail2 : AppCompatActivity(), RecipeStepFragment.OnStepClickListener {

    lateinit var  recipe :Recipes
     var twoPane:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val linearLayoutCheck:LinearLayout? = findViewById(R.id.video_description_linear_layout)
        // Tablet is connected
        if(linearLayoutCheck!=null){
       // val linearLayoutCheck = null
        //if(linearLayoutCheck!=null){
            twoPane=true
            if(intent!=null){
                 recipe= intent.getSerializableExtra(getString(R.string.main_activity_bundle_recipe)) as Recipes
            }
            val recipeStepFragment = RecipeStepFragment()
            recipeStepFragment.setRecipe(recipe)

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_container, recipeStepFragment)
                    .commit()
            val videoFragment = VideoFragment()
            videoFragment.setUrlToDisplay("")
            fragmentManager.beginTransaction()
                    .add(R.id.video_container, videoFragment)
                    .commit()
            val recipeStepDescriptionFragment = RecipeStepDescriptionFragment()
            recipeStepDescriptionFragment.setDescription("")
            fragmentManager.beginTransaction()
                    .add(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()
        }else{
            twoPane = false
            val recipeStepFragment = RecipeStepFragment()
            val fragmentManager = supportFragmentManager
            if (savedInstanceState == null) {
                Timber.v("savedinstance is null")
                if (intent != null) {
                    //After click in main activity, it sends a recipe object in intent.
                    recipe = intent.getSerializableExtra(getString(R.string.main_activity_bundle_recipe)) as Recipes
                    recipeStepFragment.setRecipe(recipe)
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_steps_container, recipeStepFragment)
                            .commit()
                } else {
                    //finish();
                }
            }
        }
    }
//    fun <T : View> Activity.bind(@IdRes res : Int) : T? {
//        @Suppress("UNCHECKED_CAST")
//        return findViewById(res) as T
//    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Timber.v("saving recipe before exiting")
        super.onSaveInstanceState(outState)
    }

    override fun onStepSelected(description: String?, videoUrl: String?) {
        // If twoPane is not inflated then send intent, else inflate video and description fragments and send data
        if (!twoPane) {
            val intent = Intent(this, RecipeStepDetail2::class.java)
            intent.putExtra(getString(R.string.recipe_detail_intent_description), description)
            intent.putExtra(getString(R.string.recipe_detail_intent_url), videoUrl)
            startActivity(intent)
        } else {
            val fragmentManager = supportFragmentManager
            val videoFragment =  VideoFragment()

            videoFragment.setUrlToDisplay(videoUrl)
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit()

            val recipeStepDescriptionFragment = RecipeStepDescriptionFragment()
            recipeStepDescriptionFragment.setDescription(description)
            fragmentManager.beginTransaction()
                    .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home-> {
                val upIntent = Intent(this, RecipeDetail2::class.java)
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so
                    // create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder
                            .create(this)
                            .addNextIntent( Intent (this, Main2Activity::class.java))
                            .addNextIntent(upIntent).startActivities()
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent)
                }
                return true
            }
        }
        //return super.onOptionsItemSelected(item);
        return true
    }
}
