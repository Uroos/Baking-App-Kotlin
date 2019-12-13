package com.example.home.mybakingappone.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.AppDatabase2
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.model.Steps2
import com.google.gson.Gson
import timber.log.Timber
import java.io.Serializable


class RecipeDetail2 : AppCompatActivity(), RecipeStepFragment2.OnStepClickListener {

    var recipe: Recipes2? = null
    var twoPane: Boolean = false
    lateinit var appDb: AppDatabase2
    var allRecipes:ArrayList<Recipes2>? = null
    var linearLayoutCheck: LinearLayout? = null
    var currentIdForSentRecipe: Int = 0
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var sizeOfDatabase:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        //this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initViews()

        // Tablet is connected
        if (linearLayoutCheck != null) {
            twoPane = true
            viewPager!!.visibility = View.GONE

            if (intent != null) {
                var bundle: Bundle = intent.getBundleExtra(getString(R.string.intent_extra_bundle))
                recipe = bundle.getSerializable(getString(R.string.main_activity_bundle_recipe)) as Recipes2
                // Toast.makeText(this, "recipe number is=" + recipe!!.name, Toast.LENGTH_SHORT).show()
            }
            val fragmentManager = supportFragmentManager

            // Inflate all three fragments that will be displayed in this activity
            val recipeStepFragment: RecipeStepFragment2 = RecipeStepFragment2.newInstance(recipe!!)
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_container, recipeStepFragment)
                    .commit()

            val videoFragment = VideoFragment2()
            videoFragment.setUrlToDisplay1("")
            fragmentManager.beginTransaction()
                    .add(R.id.video_container, videoFragment)
                    .commit()

            val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2.newInstance("", "", 0)
            fragmentManager.beginTransaction()
                    .add(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()
        } else {
            twoPane = false
//            tabLayout!!.visibility= View.VISIBLE
            viewPager!!.visibility = View.VISIBLE
            setupViewPager()

            if (savedInstanceState == null) {
                Timber.v("savedinstance is null")
                if (intent != null) {
                    //After click in main activity, it sends a recipe object in intent.
                    //We want to get id from it so we can load the clicked recipe in the viewpager
                    if (intent.hasExtra(getString(R.string.intent_extra_bundle))) {
                        var bundle = intent.getBundleExtra(getString(R.string.intent_extra_bundle))
                        recipe = bundle.getSerializable(getString(R.string.main_activity_bundle_recipe)) as Recipes2
                        currentIdForSentRecipe = recipe!!.id
                        viewPager!!.setCurrentItem(currentIdForSentRecipe-1,true) // If 1st recipe then current item is on 0

                    }
                }
            }
        }
    }

    private fun initViews() {
        appDb = AppDatabase2.getsInstance(this)
        allRecipes = appDb.taskDao().loadAllRecipes() as ArrayList<Recipes2>?
        sizeOfDatabase = allRecipes!!.size
        linearLayoutCheck= findViewById(R.id.video_description_linear_layout)
        //tabLayout = findViewById(R.id.tabs)
        viewPager=findViewById(R.id.view_pager)
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        Timber.v("saving recipe before exiting")
        outState!!.putSerializable("recipes", allRecipes as Serializable)
        super.onSaveInstanceState(outState)
    }

    override fun onStepSelected(description: String, shortDescription: String, videoUrl: String?, position: Int, steps: List<Steps2>,recipe:Recipes2) {
        // If twoPane is not inflated then send intent, else inflate video and description fragments and send data
        if (!twoPane) {
            val intent = Intent(this, RecipeStepDetail2::class.java)
            var bundle = Bundle()
            bundle.putSerializable(getString(R.string.main_activity_bundle_recipe), recipe)
            intent.putExtra(getString(R.string.intent_extra_bundle), bundle)
            intent.putExtra(getString(R.string.recipe_detail_intent_description), description)
            intent.putExtra(getString(R.string.recipe_detail_intent_url), videoUrl)
            intent.putExtra(getString(R.string.recipe_detail_intent_short_description), shortDescription)
            intent.putExtra(getString(R.string.recipe_detail_intent_position), position)
            val json = Gson().toJson(steps)
            intent.putExtra(getString(R.string.recipe_detail_intent_list_steps), json)
            startActivity(intent)
        } else {
            val fragmentManager = supportFragmentManager
            val videoFragment = VideoFragment2()

            videoFragment.setUrlToDisplay1(videoUrl)
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit()

            val recipeStepDescriptionFragment:RecipesStepDescriptionFragment2 = RecipesStepDescriptionFragment2.newInstance(description,shortDescription,position)
            fragmentManager.beginTransaction()
                    .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()
        }
    }
    private fun setupViewPager() {
        val adapter = MyViewPagerAdapter(supportFragmentManager)
        var fragmentList:ArrayList<RecipeStepFragment2> = ArrayList()

        //Creating instances of all the fragments viewpager will display and storing in a list
        (0 until sizeOfDatabase).forEach { i ->
            var firstFragmet: RecipeStepFragment2 = RecipeStepFragment2.newInstance(allRecipes!![i])
            fragmentList.add(firstFragmet)
        }
        adapter.addFragmentList(fragmentList)

        viewPager!!.adapter = adapter
        //tabLayout!!.setupWithViewPager(viewPager)
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
