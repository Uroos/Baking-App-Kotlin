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
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.PersistableBundle
import android.support.v4.app.FragmentManager
import android.util.Log
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.model.Steps2


class RecipeStepDetail2 : AppCompatActivity() {
    var description: String = ""
    var shortDescription: String = ""
    var videourl: String? = null
    var position:Int = 0    // Position of the clicked step
    var steps:ArrayList<Steps2>?=null
    var recipe:Recipes2=Recipes2()

    var i:Int=0
    lateinit var fragmentManager :FragmentManager
    lateinit var videoFragment:VideoFragment2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_step_detail)

        //If version is greater than 23 then it will turn status bar to white with grey icons else
        // status bar will be colorPrimaryDark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
         fragmentManager = supportFragmentManager

        //Restore the fragment's instance
            if (intent != null) {
                var bundle: Bundle = intent.getBundleExtra(getString(R.string.intent_extra_bundle))
                recipe = bundle.getSerializable(getString(R.string.main_activity_bundle_recipe)) as Recipes2
                description = intent.getStringExtra(getString(R.string.recipe_detail_intent_description))
                shortDescription = intent.getStringExtra(getString(R.string.recipe_detail_intent_short_description))
                videourl = intent.getStringExtra(getString(R.string.recipe_detail_intent_url))
                position = intent.getIntExtra(getString(R.string.recipe_detail_intent_position), 0)
                i = position
                val json = intent.getStringExtra(getString(R.string.recipe_detail_intent_list_steps))
                val token = object : TypeToken<ArrayList<Steps2>>() {}
                steps = Gson().fromJson<ArrayList<Steps2>>(json, token.type)
                intent = null
                Toast.makeText(this, "recipe name is: "+ recipe.name, Toast.LENGTH_SHORT).show()
            }
        if(savedInstanceState==null) {
            //If I don't put this in this if block then new fragment is created at every rotation resulting in playback position reset to 0.
            Log.v("RecipeStepDetail2", "position new video fragment created")
            videoFragment = VideoFragment2.newInstance(videourl)
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit()
        }

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //this if is not called with following file added to the manifest, which DOES save the playback position.
            //android:configChanges="orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()

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

            //val fragmentManager1 = supportFragmentManager
            val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2.newInstance(description, shortDescription,position)
            // Add places new fragment on top of old one. So I have put in replace
            fragmentManager.beginTransaction()
                    .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit()

            v.setOnClickListener(){
                i += 1
                if(i<steps!!.size) {
                    val videoFragmentn = VideoFragment2.newInstance(steps!!.get(i).videoURL)
                    fragmentManager.beginTransaction()
                            .replace(R.id.video_container, videoFragmentn)
                            .commit()
                    val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2.newInstance(steps!!.get(i).description, steps!!.get(i).shortDescription,i)
                    // Add places new fragment on top of old one. So I have put in replace
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
                            .commit()
                }else{
                    Toast.makeText(this,"No more steps",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState!!.putString("url",videourl)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
