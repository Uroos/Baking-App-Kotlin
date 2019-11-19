package com.example.home.mybakingappone.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.home.mybakingappone.R;
import com.example.home.mybakingappone.model.Steps2;

import java.util.List;

public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.recipeStepViewHolder> {

    // Keeps track of the context and list of images to display
    private Context context;
    private List<Steps2> steps;
    private final RecipeStepListAdapter.RecipeStepListAdapterOnClickHandler clickHandler;

    // The interface that receives onClick messages
    public interface RecipeStepListAdapterOnClickHandler {
        void onStepClick(Steps2 step);
    }

    /**
     * Constructor method
     *
     * @param steps The list of steps to display
     */
    public RecipeStepListAdapter(Context context, List steps, RecipeStepListAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.steps = steps;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public recipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_step_item_layout, parent, false);
        return new recipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recipeStepViewHolder holder, int position) {
        holder.setData(steps.get(position));

    }

    @Override
    public int getItemCount() {
        if (steps == null || steps.size() == 0) {
            return 0;
        }
        return steps.size();
    }

    public void setStepsData(List steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public class recipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView recipeText;

        public recipeStepViewHolder(View itemView) {
            super(itemView);
            recipeText = itemView.findViewById(R.id.tv_recipe_step);
            itemView.setOnClickListener(this);
        }

        public void setData(Steps2 steps) {
            recipeText.setText(steps.getShortDescription());
        }

        @Override
        public void onClick(View view) {
            if (clickHandler != null) {
                int position = getAdapterPosition();
                clickHandler.onStepClick(steps.get(position));
            }
        }
    }
}
