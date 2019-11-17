package com.example.home.mybakingappone.ui


import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.home.mybakingappone.R
import com.example.home.mybakingappone.ui.RecipesStepDescriptionFragment2.Position.description

/**
 * A simple [Fragment] subclass.
 */
class RecipesStepDescriptionFragment2 : Fragment() {
    //@BindView(R.id.tv_recipe_step_description)
    lateinit var textViewRecipeStepDescription:TextView
    
    object Position {
        @JvmStatic var description=""
    }
    
    fun RecipeStepDescriptionFragment2() {
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipe_step_description, container, false)
        //Toast.makeText(getActivity(), "onCreateview is called", Toast.LENGTH_SHORT).show();
        if (savedInstanceState != null) {
            Position.description = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_description));
        }
        textViewRecipeStepDescription = rootView.findViewById(R.id.tv_recipe_step_description)
        textViewRecipeStepDescription.setText(description)
        return rootView
    }

    fun setDescription(description:String) {
        Position.description = description
    }

    override fun onSaveInstanceState(@NonNull outState:Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_description), Position.description);
    }
}


