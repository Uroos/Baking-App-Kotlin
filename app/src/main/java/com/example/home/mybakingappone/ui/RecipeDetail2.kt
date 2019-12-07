package com.example.home.mybakingappone.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.model.Steps2
import timber.log.Timber
import com.google.gson.Gson
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.home.mybakingappone.model.AppDatabase


class RecipeDetail2 : AppCompatActivity(), RecipeStepFragment2.OnStepClickListener {

    var recipe: Recipes2? = null
    var twoPane: Boolean = false
    lateinit var appDb:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        //this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        appDb = AppDatabase.getsInstance(this)

        val linearLayoutCheck: LinearLayout? = findViewById(R.id.video_description_linear_layout)
        // Tablet is connected
        if (linearLayoutCheck != null) {
            twoPane = true
            if (intent != null) {
                var bundle: Bundle = intent.getBundleExtra(getString(R.string.intent_extra_bundle))
                 recipe = bundle.getSerializable(getString(R.string.main_activity_bundle_recipe)) as Recipes2
               // Toast.makeText(this, "recipe number is=" + recipe!!.name, Toast.LENGTH_SHORT).show()
            }
            val fragmentManager = supportFragmentManager
            // Inflate all three fragments that will be displayed in this activity

            val recipeStepFragment = RecipeStepFragment2()
            recipeStepFragment.setRecipe(recipe!!)
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_container, recipeStepFragment)
                    .commit()
            val videoFragment = VideoFragment2()
            videoFragment.setUrlToDisplay1("")
            fragmentManager.beginTransaction()
                    .add(R.id.video_container, videoFragment)
                    .commit()
            val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2()
            recipeStepDescriptionFragment.setDescription("","",0)
            fragmentManager.beginTransaction()
                    .add(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()
        } else {
            twoPane = false

            val recipeStepFragment = RecipeStepFragment2()
            val fragmentManager = supportFragmentManager
            if (savedInstanceState == null) {
                Timber.v("savedinstance is null")
                if (intent != null) {
                    //After click in main activity, it sends a recipe object in intent.
                    if (intent.hasExtra(getString(R.string.intent_extra_bundle))) {
                        var bundle = intent.getBundleExtra(getString(R.string.intent_extra_bundle))
                        recipe = bundle.getSerializable(getString(R.string.main_activity_bundle_recipe)) as Recipes2
                        recipeStepFragment.setRecipe(recipe!!)
                        fragmentManager.beginTransaction()
                                .add(R.id.recipe_steps_container, recipeStepFragment)
                                .commit()
                        val id = recipe!!.id
                        val name = appDb.taskDao().getRecipe(id)
                        //Toast.makeText(this, "Loaded from room. Recipe name is: "+name,Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(this, "Recipe has no extra", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    //finish();
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Timber.v("saving recipe before exiting")
        super.onSaveInstanceState(outState)
    }

    override fun onStepSelected(description: String,shortDescription:String, videoUrl: String?,position:Int, steps: List<Steps2>) {
        // If twoPane is not inflated then send intent, else inflate video and description fragments and send data
        if (!twoPane) {
            val intent = Intent(this, RecipeStepDetail2::class.java)
            intent.putExtra(getString(R.string.recipe_detail_intent_description), description)
            intent.putExtra(getString(R.string.recipe_detail_intent_url), videoUrl)
            intent.putExtra(getString(R.string.recipe_detail_intent_short_description),shortDescription)
            intent.putExtra(getString(R.string.recipe_detail_intent_position),position)
            val json=Gson().toJson(steps)
            intent.putExtra(getString(R.string.recipe_detail_intent_list_steps),json)
            startActivity(intent)
        } else {
            val fragmentManager = supportFragmentManager
            val videoFragment = VideoFragment2()

            videoFragment.setUrlToDisplay1(videoUrl)
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit()

            val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2()
            recipeStepDescriptionFragment.setDescription(description,shortDescription,position)
            fragmentManager.beginTransaction()
                    .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                val upIntent = Intent(this, RecipeDetail2::class.java)
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so
                    // create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder
                            .create(this)
                            .addNextIntent(Intent(this, Main2Activity::class.java))
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
        return true
    }
}
