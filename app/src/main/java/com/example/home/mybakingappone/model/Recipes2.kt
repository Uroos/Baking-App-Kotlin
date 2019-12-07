package com.example.home.mybakingappone.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "recipes")
class Recipes2 ():java.io.Serializable {

    @PrimaryKey
    var id: Int=0
    var name: String=""
    var ingredients: ArrayList<Ingredients2>? = ArrayList()
    var steps: ArrayList<Steps2>? = ArrayList()
    var servings: Int=0
    var image: String=""
    var favorite: Boolean = false
    var recentlyviewed:Boolean = false

    constructor(id:Int,
                name:String,
                ingredients: ArrayList<Ingredients2>?,
                steps:ArrayList<Steps2>?,
                servings:Int,
                image:String,
                favorite:Boolean,
                recentlyviewed:Boolean):this(){
        this.id=id
        this.name=name
        this.ingredients=ingredients
        this.steps=steps
        this.servings=servings
        this.image=image
        this.favorite=favorite
        this.recentlyviewed=recentlyviewed
    }
//    constructor(id:Int, name:String,favorite:Boolean,recently_viewed:Boolean):this(id,name, null,null,0, ""){
//        this.favorite=favorite
//        this.recentlyviewed = recently_viewed
//    }
}