package com.example.home.mybakingappone.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.AppDatabase2
import com.example.home.mybakingappone.model.Recipes2
import com.example.home.mybakingappone.model.Steps2

import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.content_favorite.*

class FavoriteActivity : AppCompatActivity() {
    private var favRecipes: List<Recipes2> = ArrayList()
    private var linearLayoutManagerFavorite: LinearLayoutManager? = null
    private var favoriteAdapter: FavoriteAdapter? = null
    private lateinit var db : AppDatabase2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setSupportActionBar(toolbar)

        db = AppDatabase2.getsInstance(this)

        favRecipes=db.taskDao().loadFavoriteRecipes()
        linearLayoutManagerFavorite = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_favorite_recent.layoutManager = linearLayoutManagerFavorite
        rv_favorite_recent.hasFixedSize()
        favoriteAdapter= FavoriteAdapter(this,favRecipes)
        rv_favorite_recent.adapter=favoriteAdapter

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Do you want to delete all the recipes?", Snackbar.LENGTH_LONG)
                    .setAction(
                            "Yes"
                    ) {
                        db.taskDao().deleteAllFavorites()
                        rv_favorite_recent.adapter=null
                        Toast.makeText(this, "Recipes deleted!",Toast.LENGTH_SHORT).show()
                    }.show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
//    class DeleteListener : View.OnClickListener {
//
//        override fun onClick(v: View) {
//            // Code to delete recipes from database
//            val db = AppDatabase.getsInstance(v.context)
//            db.taskDao().deleteAll()
//            Toast.makeText(v.context, "Recipes deleted!",Toast.LENGTH_SHORT).show()
//        }
//    }

}
