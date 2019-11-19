package com.example.home.mybakingappone.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.home.mybakingappone.R;
//import com.example.home.mybakingappone.model.Recipes;
import com.example.home.mybakingappone.model.Recipes2;
import com.squareup.picasso.Picasso;

import java.util.List;
//Notes
//Changed all Recipes instances to Recipes2 for kotlin interfacing
// Custom adapter class that displays a list of Android-Me images in a GridView
public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.recipeViewHolder> {

    // Keeps track of the context and list of images to display
    private Context context;
    private List<Recipes2> recipes;
    private final RecipeListAdapterOnClickHandler clickHandler;

    // The interface that receives onClick messages
    public interface RecipeListAdapterOnClickHandler {
        void onRecipeClick(Recipes2 recipe);
    }

    /**
     * Constructor method
     *
     * @param recipes The list of recipes to display
     */
    public MasterListAdapter(Context context, List recipes, RecipeListAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.recipes = recipes;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public recipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_grid_item_layout, parent, false);
        return new recipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recipeViewHolder holder, int position) {
        holder.setData(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        if (recipes == null || recipes.size() == 0) {
            return 0;
        }
        return recipes.size();
    }

    public void setRecipeData(List recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public class recipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView recipeImage;
        public TextView recipeText;

        public recipeViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeText = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        public void setData(Recipes2 recipe) {
            Picasso.with(context).load(Integer.parseInt(recipe.getImage())).error(R.mipmap.ic_launcher).into(recipeImage);
            recipeText.setText(recipe.getName());
        }

        @Override
        public void onClick(View view) {
            if (clickHandler != null) {
                int position = getAdapterPosition();
                clickHandler.onRecipeClick(recipes.get(position));
            }
        }
    }
}
