package com.example.home.mybakingappone.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Recipes2

class FavoriteAdapter() : RecyclerView.Adapter<FavoriteAdapter.favoriteViewHolder>() {
    private lateinit var context: Context
    private lateinit var recipes: List<Recipes2>

    constructor(context: Context, recipes: List<Recipes2>) : this() {
        this.context = context
        this.recipes = recipes
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item_layout, parent, false)
        return favoriteViewHolder(view)    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: favoriteViewHolder, position: Int) {
       holder.recipeNameText.setText(recipes.get(position).name)
    }
    class favoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeNameText = itemView.findViewById(R.id.tv_favorite_name) as TextView
    }
}