package com.example.home.mybakingappone.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Recipes2
import com.squareup.picasso.Picasso

class MasterListAdapter2() : RecyclerView.Adapter<MasterListAdapter2.recipeViewHolder>() {

    // Keeps track of the context and list of images to display
    private lateinit var context: Context
    private lateinit var recipes: List<Recipes2>
    public lateinit var clickHandler: RecipeListAdapterOnClickHandler

    // The interface that receives onClick messages
    interface RecipeListAdapterOnClickHandler {
        fun onRecipeClick(recipe: Recipes2)
    }

    constructor(context: Context, recipes: List<Recipes2>, clickHandler: RecipeListAdapterOnClickHandler) : this() {
        this.context = context
        this.recipes = recipes
        this.clickHandler = clickHandler
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_grid_item_layout, parent, false)
        return recipeViewHolder(view)
    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: recipeViewHolder, position: Int) {
        //holder.setData(recipes.get(position))
        Picasso.with(context).load((recipes.get(position).image).toInt()).error(R.mipmap.ic_launcher).into(holder.recipeImage)
        holder.recipeText.setText(recipes.get(position).name)
        holder.itemView.setOnClickListener {
            clickHandler.onRecipeClick(recipes.get(position))
        }
    }

    fun setRecipeData(recipes: List<Recipes2>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    class recipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage = itemView.findViewById(R.id.recipe_image) as ImageView
        val recipeText = itemView.findViewById(R.id.recipe_name) as TextView
    }
}
