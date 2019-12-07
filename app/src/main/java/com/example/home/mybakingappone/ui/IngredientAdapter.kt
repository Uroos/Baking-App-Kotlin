package com.example.home.mybakingappone.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.widget.ImageView
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Ingredients2


class IngredientAdapter(): Adapter<IngredientAdapter.ingredientViewHolder>() {
    // Keeps track of the context and list of images to display
    private lateinit var context: Context
    private lateinit var ingredients: List<Ingredients2>

    constructor(context: Context, ingredients: List<Ingredients2>) : this() {
        this.context = context
        this.ingredients = ingredients
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ingredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredients_listview, parent, false)
        return ingredientViewHolder(view)
    }

    override fun getItemCount() =ingredients.size

    override fun onBindViewHolder(holder: ingredientViewHolder, position: Int) {
        val ingredientItem=(ingredients.get(position).quantity).toString() + " " + ingredients.get(position).measure + " " + ingredients.get(position).ingredient
        holder.ingredientText.setText(ingredientItem)
    }
    fun setIngredientsData(ingredients: List<Ingredients2>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }
    class ingredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientText = itemView.findViewById(R.id.tv_ingredient_name) as TextView
    }
}