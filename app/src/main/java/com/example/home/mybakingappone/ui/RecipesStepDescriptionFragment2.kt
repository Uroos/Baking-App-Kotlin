package com.example.home.mybakingappone.ui


import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.home.mybakingappone.R

//import com.example.home.mybakingappone.ui.RecipesStepDescriptionFragment2.Position.description
//import com.example.home.mybakingappone.ui.RecipesStepDescriptionFragment2.Position.shortDescription

class RecipesStepDescriptionFragment2 : Fragment() {
    lateinit var textViewRecipeStepDescription: TextView
    lateinit var textViewRecipeStepShortDescription: TextView

    lateinit var description: String
    lateinit var shortDescription: String
//    object Position {
//        @JvmStatic
//        var description = ""
//        @JvmStatic
//        var shortDescription=""
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipe_step_description, container, false)
        textViewRecipeStepDescription = rootView.findViewById(R.id.tv_recipe_step_description)
        textViewRecipeStepShortDescription = rootView.findViewById(R.id.tv_recipe_step_short_description)
        if (savedInstanceState != null) {
            description = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_description))
            shortDescription = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_short_description))
            textViewRecipeStepShortDescription.setText("")
            textViewRecipeStepDescription.setText("")
        }
        textViewRecipeStepDescription.setText(description)
        // For making a line under the text
        var content = SpannableString(shortDescription)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        textViewRecipeStepShortDescription.setText(content)
        return rootView
    }

    //    fun setDescription(description: String,shortDescription:String) {
//        Position.description = description
//        Position.shortDescription=shortDescription
//    }
    fun setDescription(description: String, shortDescription: String) {
        this.description = description
        this.shortDescription = shortDescription
    }

    override fun onSaveInstanceState(@NonNull outState: Bundle) {
        super.onSaveInstanceState(outState)
        // outState.putString(getString(R.string.recipe_step_description_fragment_outstate_description), Position.description)
        // outState.putString(getString(R.string.recipe_step_description_fragment_outstate_short_description),Position.shortDescription)
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_description), description)
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_short_description), shortDescription)
    }
}



