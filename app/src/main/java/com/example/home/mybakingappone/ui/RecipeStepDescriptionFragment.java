package com.example.home.mybakingappone.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.home.mybakingappone.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepDescriptionFragment extends Fragment {
    private static String description;
    private Unbinder unbinder;
    @BindView(R.id.tv_recipe_step_description)
    TextView textViewRecipeStepDescription;

    public RecipeStepDescriptionFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_description, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        //Toast.makeText(getActivity(), "onCreateview is called", Toast.LENGTH_SHORT).show();
        if (savedInstanceState != null) {
            description = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_description));
        }
        textViewRecipeStepDescription.setText(description);
        return rootView;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_description), description);
    }

}
