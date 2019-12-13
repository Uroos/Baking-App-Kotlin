package com.example.home.mybakingappone.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.AppDatabase2
import com.example.home.mybakingappone.model.Recipes2

import kotlinx.android.synthetic.main.activity_recently_viewed.*
import kotlinx.android.synthetic.main.content_favorite.*

class RecentlyViewedActivity : AppCompatActivity() {
    private var recentRecipes: List<Recipes2> = ArrayList()
    private var linearLayoutManagerRecentlyViewed: LinearLayoutManager? = null
    private var favoriteAdapter: FavoriteAdapter? = null
    private lateinit var db : AppDatabase2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase2.getsInstance(this)

        recentRecipes=db.taskDao().loadRecentRecipes()
        linearLayoutManagerRecentlyViewed = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_favorite_recent.layoutManager = linearLayoutManagerRecentlyViewed
        rv_favorite_recent.hasFixedSize()
        favoriteAdapter= FavoriteAdapter(this,recentRecipes)
        rv_favorite_recent.adapter=favoriteAdapter

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Do you want to delete all the recipes?", Snackbar.LENGTH_LONG)
                    .setAction(
                            "Yes"
                    ) {
                        db.taskDao().deleteAllRecents()
                        rv_favorite_recent.adapter=null
                        Toast.makeText(this, "Recipes deleted!", Toast.LENGTH_SHORT).show()
                    }.show()
        }
    }
}
