package com.example.home.mybakingappone.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.home.mybakingappone.R

class RecipeStepDetail2 : AppCompatActivity() {
    var description: String=""
    var videourl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_step_detail)

        //Restore the fragment's instance
        if (this.intent != null) {
            description = intent.getStringExtra(getString(R.string.recipe_detail_intent_description))
            videourl = intent.getStringExtra(getString(R.string.recipe_detail_intent_url))
        }
        val videoFragment = VideoFragment2()
        videoFragment.setUrlToDisplay1(videourl)
        videoFragment.setPlaybackPosition1(0)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .add(R.id.video_container, videoFragment)
                .commit()

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2()
            recipeStepDescriptionFragment.setDescription(description)
            val fragmentManager1 = supportFragmentManager
            fragmentManager1.beginTransaction()
                    .add(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
    }
}
