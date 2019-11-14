package com.example.home.mybakingappone.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.home.mybakingappone.R;
import com.example.home.mybakingappone.RecipeUpdateService;
import com.example.home.mybakingappone.model.Ingredients;
import com.example.home.mybakingappone.model.Recipes;
import com.example.home.mybakingappone.model.Steps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class RecipeStepFragment extends Fragment implements RecipeStepListAdapter.RecipeStepListAdapterOnClickHandler {

    @BindView(R.id.tv_recipe_name)
    TextView textViewRecipeName;
    @BindView(R.id.rv_recipe_step)
    RecyclerView recyclerViewSteps;
    @BindView(R.id.tv_ingredients)
    TextView textViewIngredients;
    //@BindView(R.id.btn_heart)
    //ToggleButton toggleButton;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    Context context;
    private Unbinder unbinder;
    private String ingredients;
    private static Recipes recipe;

    private List<Steps> steps = new ArrayList<>();
    ArrayList<Ingredients> ingredientsList = new ArrayList<>();
    private RecipeStepListAdapter recipeStepListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private Parcelable savedStepsRecyclerLayoutState;
    private Bundle bundleRecyclerViewState;
    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    // Method in interface to handle recycler view clicks
    @Override
    public void onStepClick(Steps step) {
        String tempVideoUrl = step.getVideoURL();
        String tempThumbNailUrl = step.getThumbnailURL();
        String urlToSend = "";
        if (tempVideoUrl == null || TextUtils.isEmpty(tempVideoUrl)) {
            if (tempThumbNailUrl == null || TextUtils.isEmpty(tempThumbNailUrl)) {
                urlToSend = null;
            } else {
                urlToSend = tempThumbNailUrl;
            }
        } else {
            urlToSend = tempVideoUrl;
        }
        mCallback.onStepSelected(step.getDescription(), urlToSend);
    }

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnStepClickListener {
        void onStepSelected(String description, String videoUrl);
    }

    //Empty constructor is necessary for instantiating the fragment
    public RecipeStepFragment() {

    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    //Inflates the fragment layout and sets any resources
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        // Inflate the RecipeStepFragment layout
        unbinder = ButterKnife.bind(this, rootView);
        if (savedInstanceState == null) {
            Timber.v("savedinstancestate is null. trying to get stored recipe");
            steps = recipe.getSteps();
            ingredientsList = recipe.getIngredients();
        } else {
            recipe = (Recipes) savedInstanceState.getSerializable("recipe");
            steps = recipe.getSteps();
            ingredientsList = recipe.getIngredients();
        }
        textViewRecipeName.setText(recipe.getName());
        ingredients = arrangeIngredientList(ingredientsList);
        textViewIngredients.setText(ingredients);

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewSteps.setLayoutManager(linearLayoutManager);
        recyclerViewSteps.setHasFixedSize(true);
        recipeStepListAdapter = new RecipeStepListAdapter(context, steps, this);
        recyclerViewSteps.setAdapter(recipeStepListAdapter);

        return rootView;
    }

    private String arrangeIngredientList(ArrayList<Ingredients> ingredientsList) {
        StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        for (int i = 0; i < ingredientsList.size(); i++) {
            String quantity = String.valueOf(ingredientsList.get(i).getQuantity());
            String measure = ingredientsList.get(i).getMeasure();
            String ingredient = ingredientsList.get(i).getIngredient();
            stringBuilder.append(quantity)
                    .append(" ")
                    .append(measure)
                    .append("    ")
                    .append(ingredient)
                    .append("\n");
        }
        result = stringBuilder.toString();
        Timber.v("Ingredient list is: " + result);
        return result;
    }

    @OnClick(R.id.fab)
    public void onClick(FloatingActionButton fab) {
        //RecipeUpdateService.startRecipeUpdate(context, ingredients, recipe);
        RecipeUpdateService.startRecipeUpdate(context, ingredients, recipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("recipe", (Serializable) recipe);
        outState.putString("ingredients", ingredients);
        //outState.putParcelable(BUNDLE_STEPS_RECYCLER_LAYOUT, recyclerViewSteps.getLayoutManager().onSaveInstanceState());
    }

    public void setRecipe(Recipes recipe) {
        this.recipe = recipe;
    }
}
