package com.example.home.mybakingappone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.home.mybakingappone.R;
import com.example.home.mybakingappone.model.Recipes;

import java.io.Serializable;

import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeDetail extends AppCompatActivity implements RecipeStepFragment.OnStepClickListener {

    private static Recipes recipe;
    private static String videoUrl;
    private static String description;
    private Boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Tablet is connected
        if (findViewById(R.id.video_description_linear_layout) != null) {
            twoPane = true;

            if (getIntent() != null) {
                recipe = (Recipes) getIntent().getSerializableExtra(getString(R.string.main_activity_bundle_recipe));
            }
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setRecipe(recipe);

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_container, recipeStepFragment)
                    .commit();
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setUrlToDisplay("");
            //android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.video_container, videoFragment)
                    .commit();
            RecipeStepDescriptionFragment recipeStepDescriptionFragment = new RecipeStepDescriptionFragment();
            recipeStepDescriptionFragment.setDescription("");
            //android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit();

        } else {
            twoPane = false;
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            if (savedInstanceState == null) {
                Timber.v("savedinstance is null");
                if (getIntent() != null) {
                    //After click in main activity, it sends a recipe object in intent.
                    recipe = (Recipes) getIntent().getSerializableExtra(getString(R.string.main_activity_bundle_recipe));
                    recipeStepFragment.setRecipe(recipe);
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_steps_container, recipeStepFragment)
                            .commit();
                } else {
                    //finish();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Timber.v("saving recipe before exiting");
        //outState.putSerializable("recipe", (Serializable) recipe);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStepSelected(String description, String videoUrl) {
        // If twoPane is not inflated then send intent, else inflate video and description fragments and send data
        if (!twoPane) {
            Intent intent = new Intent(this, RecipeStepDetail2.class);
            intent.putExtra(getString(R.string.recipe_detail_intent_description), description);
            intent.putExtra(getString(R.string.recipe_detail_intent_url), videoUrl);
            startActivity(intent);
        } else {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setUrlToDisplay(videoUrl);
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit();

            RecipeStepDescriptionFragment recipeStepDescriptionFragment = new RecipeStepDescriptionFragment();
            recipeStepDescriptionFragment.setDescription(description);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = new Intent(this, RecipeDetail.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so
                    // create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder
                            .from(this)
                            .addNextIntent(new Intent(this, MainActivity.class))
                            .addNextIntent(upIntent).startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        //return super.onOptionsItemSelected(item);
        return true;
    }
}
