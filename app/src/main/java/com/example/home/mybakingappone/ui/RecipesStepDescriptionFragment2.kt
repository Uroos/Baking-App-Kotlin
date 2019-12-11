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
import com.example.home.mybakingappone.model.Recipes2

//import com.example.home.mybakingappone.ui.RecipesStepDescriptionFragment2.Position.description
//import com.example.home.mybakingappone.ui.RecipesStepDescriptionFragment2.Position.shortDescription

class RecipesStepDescriptionFragment2 : Fragment() {
    lateinit var textViewRecipeStepDescription: TextView
    lateinit var textViewRecipeStepShortDescription: TextView

     var description: String = ""
    var shortDescription: String = ""
    var stepNumber:Int = 0

    companion object {
        fun newInstance(ndescription: String,nshortDescription: String,nposition:Int): RecipesStepDescriptionFragment2 {
            val f = RecipesStepDescriptionFragment2()
            val bundle = Bundle()
            bundle.putString("description",ndescription)
            bundle.putString("short_description",nshortDescription)
            bundle.putInt("position",nposition)
            f.arguments = bundle
            return f
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipe_step_description, container, false)
        textViewRecipeStepDescription = rootView.findViewById(R.id.tv_recipe_step_description)
        textViewRecipeStepShortDescription = rootView.findViewById(R.id.tv_recipe_step_short_description)
        if(savedInstanceState==null){
            description=arguments!!.getString("description") as String
            shortDescription=arguments!!.getString("short_description")
            stepNumber=arguments!!.getInt("position")

        }else {
            description = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_description))
            shortDescription = savedInstanceState.getString(getString(R.string.recipe_step_description_fragment_outstate_short_description))
            textViewRecipeStepShortDescription.setText("")
            textViewRecipeStepDescription.setText("")
        }
        textViewRecipeStepDescription.setText(description)

        // For making a line under the text
        if(stepNumber==0){
        var content = SpannableString(shortDescription)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        textViewRecipeStepShortDescription.setText(content)
        }else{
            var content = SpannableString("Step "+stepNumber)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            textViewRecipeStepShortDescription.setText(content)
        }
        return rootView
    }

    override fun onSaveInstanceState(@NonNull outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_description), description)
        outState.putString(getString(R.string.recipe_step_description_fragment_outstate_short_description), shortDescription)
    }
}



