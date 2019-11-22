package com.example.home.mybakingappone.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.home.mybakingappone.R
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.service.autofill.Validators.or
import android.support.v7.app.ActionBar
import android.view.WindowManager
import android.support.v7.widget.Toolbar
import android.view.View


class RecipeStepDetail2 : AppCompatActivity() {
    var description: String = ""
    var shortDescription:String=""
    var videourl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_step_detail)

        //If version is greater than 23 then it will turn status bar to white with grey icons else
        // status bar will be colorPrimaryDark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        //Restore the fragment's instance
        if (this.intent != null) {
            description = intent.getStringExtra(getString(R.string.recipe_detail_intent_description))
            shortDescription=intent.getStringExtra(getString(R.string.recipe_detail_intent_short_description))
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
            // Only set the toolbar when in portrait mode.
            var toolbar:Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toolbar.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END)

            val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2()
            recipeStepDescriptionFragment.setDescription(description,shortDescription)
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
}
