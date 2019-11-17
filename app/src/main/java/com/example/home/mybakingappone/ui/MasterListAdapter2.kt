package com.example.home.mybakingappone.ui
//
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import com.example.home.mybakingappone.R
//import com.example.home.mybakingappone.model.Recipes
//import com.squareup.picasso.Picasso
//
//class MasterListAdapter2 : RecyclerView.Adapter<MasterListAdapter2.recipeViewHolder>() {
//
//    // Keeps track of the context and list of images to display
//    private lateinit var context: Context
//    private lateinit var recipes: List<Recipes>
//    private lateinit var clickHandler: RecipeListAdapterOnClickHandler
//
//    // The interface that receives onClick messages
//    interface RecipeListAdapterOnClickHandler {
//        fun onRecipeClick(recipe: Recipes)
//    }
//
//    constructor(context: Context, recipes: List<Recipes>, clickHandler: RecipeListAdapterOnClickHandler) {
//        this.context = context
//        this.recipes = recipes
//        this.clickHandler = clickHandler
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterListAdapter2.recipeViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_grid_item_layout, parent, false) as recipeViewHolder
//        return view
//    }
//
//    override fun getItemCount() = recipes.size
//
//    override fun onBindViewHolder(holder: recipeViewHolder, position: Int) {
//        holder.setData(recipes.get(position))
//        holder.onClick(clickHandler)
//    }
//
//    fun setRecipeData(recipes: List<Recipes>) {
//        this.recipes = recipes
//        notifyDataSetChanged()
//    }
//
//    class recipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//
//        val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
//        val recipeText: TextView = itemView.findViewById(R.id.recipe_name)
//
//        fun recipeViewHolder(itemView: View) {
//            super.itemView
//            itemView.setOnClickListener(this)
//        }
//
//        fun setData(recipe: Recipes) {
//            //Picasso.with(context).load(Integer.parseInt(recipe.getImage())).error(R.mipmap.ic_launcher).into(recipeImage);
//            Picasso.with(itemView.context).load(recipe.image).error(R.mipmap.ic_launcher).into(recipeImage)
//            recipeText.setText(recipe.getName());
//        }
//
//        //        fun onClick(view: RecipeListAdapterOnClickHandler) {
////           if (view.clickHandler != null) {
//////                var position = getAdapterPosition()
//////                clickHandler.onRecipeClick(recipes.get(position));
////            }
////}
//        override fun onClick(view: View?) {
//        }
//
//    }
//}