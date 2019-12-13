package com.example.home.mybakingappone.ui

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Recipes2

class RecipeStepDetailPagerAdapter:PagerAdapter {

    var context:Context?=null
    var recipe:Recipes2= Recipes2()
    var positionOfStep:Int = 0
    lateinit var fragmentManager : FragmentManager

    constructor(context:Context,recipe:Recipes2,fragmentManager:FragmentManager,positionOfStep:Int){
        this.context=context
        this.recipe=recipe
        this.fragmentManager = fragmentManager
        this.positionOfStep = positionOfStep
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_recipe_step_detail, collection, false) as ViewGroup

        val videoFragment = VideoFragment2.newInstance(recipe.steps!!.get(position).videoURL)
        fragmentManager.beginTransaction()
                .replace(R.id.video_container, videoFragment)
                .commit()
        val recipeStepDescriptionFragment = RecipesStepDescriptionFragment2.newInstance(
                recipe.steps!!.get(position).description,
                recipe.steps!!.get(position).shortDescription,
                position)
        // Add places new fragment on top of old one. So I have put in replace
        fragmentManager.beginTransaction()
                .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
                .commit()
        collection.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View?)
    }

    override fun isViewFromObject(view: View, objectAny: Any): Boolean {
        return view === objectAny

    }

    override fun getCount(): Int {
        return recipe.steps!!.size
    }
}