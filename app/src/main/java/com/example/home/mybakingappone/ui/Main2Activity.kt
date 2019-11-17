package com.example.home.mybakingappone.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Recipes

class Main2Activity : AppCompatActivity(), MasterListFragment.OnImageClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onImageSelected(recipe: Recipes?) {
        val intent = Intent(this, RecipeDetail2::class.java)
        var bundle = Bundle()
        bundle.putSerializable(getString(R.string.main_activity_bundle_recipe), recipe)
        intent.putExtras(bundle)
        startActivity(intent)

    }
}