package com.example.home.mybakingappone.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.home.mybakingappone.R


class RecipeStepDetail2 : AppCompatActivity() {
    var description: String = ""
    var shortDescription: String = ""
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
            shortDescription = intent.getStringExtra(getString(R.string.recipe_detail_intent_short_description))
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
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            this.supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            val inflater: LayoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v: View = inflater.inflate(R.layout.custom_actionbar, null);
            val p: ActionBar.LayoutParams = ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.RIGHT)
            this.supportActionBar!!.setCustomView(v, p)
            this.supportActionBar!!.setDisplayShowTitleEnabled(true)
            this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            /////////////////////////////////////////
            val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2()
            recipeStepDescriptionFragment.setDescription(description, shortDescription)
            val fragmentManager1 = supportFragmentManager
            // Add places new fragment on top of old one. So I have put in replace
            fragmentManager1.beginTransaction()
                    .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
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
