package com.example.home.mybakingappone.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.model.Steps2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.TextView

class RecipeStepDetail2 : AppCompatActivity() {
    private var description: String = ""
    private var shortDescription: String = ""
    private var videourl: String? = null
    private var position: Int = 0    // Position of the clicked step
    private var steps: ArrayList<Steps2>? = null
    private var recipe: Recipes2 = Recipes2()
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout?=null
    private val toolbar: Toolbar? = null
    var i: Int = 0
    var viewPagerNumber:Int = 0
    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_step_detail_viewpager)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

        viewPager = findViewById(R.id.view_pager_recipe_step_detail)
        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)

        fragmentManager = supportFragmentManager
        //If version is greater than 23 then it will turn status bar to white with grey icons else
        // status bar will be colorPrimaryDark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

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
        }
            setupViewPager()
            viewPager!!.setCurrentItem(position,true)

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
           // this if is not called with following file added to the manifest, which DOES save the playback position.
           // android:configChanges="orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
           // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()

            // Only set the toolbar when in portrait mode.
            toolbar.visibility = View.VISIBLE
            setSupportActionBar(toolbar)
            this.supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            val inflater: LayoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val next: View = inflater.inflate(R.layout.custom_actionbar, null);
            val p: ActionBar.LayoutParams = ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.RIGHT)
            this.supportActionBar!!.setCustomView(next, p)
            this.supportActionBar!!.setDisplayShowTitleEnabled(true)
            this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            next.setOnClickListener() {
                i += 1
                if (i < steps!!.size) {
                    viewPager!!.setCurrentItem(i,true)

                } else {
                    Toast.makeText(this, "No more steps", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupViewPager (){
        val adapter = RecipeStepDetail2ViewPagerAdapter(supportFragmentManager)
        var fragmentListSteps:ArrayList<RecipeStepDetail2Fragment> = ArrayList()

        //Creating instances of all the fragments viewpager will display and storing in a list
        (0 until recipe.steps!!.size).forEach { i ->
            var tempFragment: RecipeStepDetail2Fragment = RecipeStepDetail2Fragment.newInstance(recipe.steps!!.get(i).shortDescription,
                    recipe.steps!!.get(i).description,
                    i,
                    recipe.steps!!.get(i).videoURL)
            fragmentListSteps.add(tempFragment)
        }
        adapter.addFragmentList(fragmentListSteps)

        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                viewPagerNumber = position
            }

            override fun onPageSelected(position: Int) {
                viewPagerNumber = position
                i = viewPagerNumber
                //Toast.makeText(this@RecipeStepDetail2, "view pager number is: " + viewPagerNumber, Toast.LENGTH_SHORT).show()
            }
        })

        for (i in 0 until tabLayout!!.tabCount) {
            Log.v("RecipeStepdetail", "size of tabs is "+tabLayout!!.tabCount)
            val tab = tabLayout!!.getTabAt(i)
            tab!!.setCustomView(getTabView(i))
        }
    }

    fun getTabView(position: Int): View {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
        val tv = v.findViewById(R.id.tv_number) as TextView
        tv.setText(position.toString())
        return v
    }
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState!!.putString("url", videourl)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

