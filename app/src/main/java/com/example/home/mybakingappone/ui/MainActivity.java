package com.example.home.mybakingappone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.home.mybakingappone.R;
import com.example.home.mybakingappone.model.Recipes;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        MasterListFragment.OnImageClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onImageSelected(Recipes recipe) {
        Intent intent = new Intent(this, RecipeDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.main_activity_bundle_recipe), recipe);
        intent.putExtras(bundle);
        Timber.v("Name of clicked recipe is " + recipe.getName());
        startActivity(intent);
    }
}
