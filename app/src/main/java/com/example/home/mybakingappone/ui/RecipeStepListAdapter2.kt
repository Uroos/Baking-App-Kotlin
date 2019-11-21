package com.example.home.mybakingappone.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.model.Steps2

class RecipeStepListAdapter2() : RecyclerView.Adapter<RecipeStepListAdapter2.recipeStepViewHolder>() {

    private lateinit var context: Context
    private lateinit var steps: List<Steps2>
    lateinit var clickHandler: RecipeStepListAdapter2.RecipeStepListAdapterOnClickHandler

    // The interface that receives onClick messages
    interface RecipeStepListAdapterOnClickHandler {
        fun onStepClick(step: Steps2)
    }

    constructor(context: Context, steps: List<Steps2>, clickHandler: RecipeStepListAdapterOnClickHandler) : this() {
        this.context = context
        this.steps = steps
        this.clickHandler = clickHandler
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recipeStepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_step_item_layout, parent, false)
        return recipeStepViewHolder(view)

    }

    override fun getItemCount() = steps.size

    override fun onBindViewHolder(holder: RecipeStepListAdapter2.recipeStepViewHolder, position: Int) {
        holder.recipeText.setText(steps.get(position).shortDescription)
        holder.itemView.setOnClickListener {
            clickHandler.onStepClick(steps.get(position))
        }
    }

    class recipeStepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeText = itemView.findViewById(R.id.tv_recipe_step) as TextView
    }

    fun setStepsData(steps: List<Steps2>) {
        this.steps = steps
        notifyDataSetChanged()
    }
}