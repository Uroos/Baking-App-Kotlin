package com.example.home.mybakingappone.model

import kotlinx.serialization.Serializable
import java.util.ArrayList

@Serializable
data class Recipes2 (val id:Int,val name:String,val ingredients:ArrayList<Ingredients2>,val steps:ArrayList<Steps2>,val servings:Int,var image:String) : java.io.Serializable {
    @Transient
    var favorite : Boolean = false
}