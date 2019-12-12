package com.example.home.mybakingappone.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Layout
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.AppDatabase2
import com.example.home.mybakingappone.model.Recipes2
import android.R.attr.data
import android.util.TypedValue
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

class Main2Activity : AppCompatActivity(), MasterListFragment2.OnImageClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
//        this.supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        val inflater: LayoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val v: View = inflater.inflate(R.layout.custom_actionbar_main, null);
//        //ActionBar.LayoutParams(int width, int height, int gravity)
//        val p: ActionBar.LayoutParams = ActionBar.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                Gravity.CENTER_HORIZONTAL)
//        this.supportActionBar!!.setCustomView(v, p)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        // Calculate ActionBar height to have the text aligned center_vertically with the icon
        val tv = TypedValue()
        var actionBarHeight=0
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        actionBarHeight /= 2
        val tv_home: TextView =findViewById(R.id.tv_home)
        tv_home.setPadding(0,actionBarHeight,0,0)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    // When hamburger icon is pressed it opens the navigation drawer.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // close drawer when item is tapped
        when(item.itemId){
            R.id.menu_favorite->{
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_recently_viewed->{
                val intent = Intent(this, RecentlyViewedActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_privacy->{
            }
            R.id.menu_about->{
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true
    }

    override fun onImageSelected(recipe: Recipes2?) {
        // Save to recently viewed database
        val db =AppDatabase2.getsInstance(this)
        val recipeRecent= Recipes2(recipe!!.id,
                recipe!!.name,
                recipe!!.ingredients,
                recipe!!.steps,
                recipe!!.servings,
                recipe!!.image,
                false,
                true)
        db.taskDao().updateRecipe(recipeRecent)

        val intent = Intent(this, RecipeDetail2::class.java)
        var bundle = Bundle()
        bundle.putSerializable(getString(R.string.main_activity_bundle_recipe), recipe)
        intent.putExtra(getString(R.string.intent_extra_bundle), bundle)
        startActivity(intent)
    }
}