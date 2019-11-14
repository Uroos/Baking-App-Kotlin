package com.example.home.mybakingappone.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.home.mybakingappone.R;

public class RecipeStepDetail extends AppCompatActivity {

    //private VideoFragment videoFragment;
    //RecipeStepFragment recipeStepFragment;
    String description;
    String videourl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        // onCreate is called on every rotation

        //Restore the fragment's instance
        if (getIntent() != null) {
            description = getIntent().getStringExtra(getString(R.string.recipe_detail_intent_description));
            videourl = getIntent().getStringExtra(getString(R.string.recipe_detail_intent_url));
        }
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setUrlToDisplay(videourl);
        FragmentManager fragmentManager = getSupportFragmentManager();
        videoFragment.playbackPosition = 0;
        fragmentManager.beginTransaction()
                .add(R.id.video_container, videoFragment)
                .commit();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            RecipeStepDescriptionFragment recipeStepDescriptionFragment = new RecipeStepDescriptionFragment();
            recipeStepDescriptionFragment.setDescription(description);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.beginTransaction()
                    .add(R.id.step_instruction_container, recipeStepDescriptionFragment)
                    .commit();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "onstart is called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Called when acivity goes to background and is then brough forward
        //Toast.makeText(this, "on resume is called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //getSupportFragmentManager().putFragment(outState, "myVideoName", videoFragment);
        //getSupportFragmentManager().putFragment(outState, "myRecipeName", recipeStepFragment);
    }
}
