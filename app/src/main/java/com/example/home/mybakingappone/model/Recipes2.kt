package com.example.home.mybakingappone.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.ArrayList

@Entity(tableName = "recipes")
@Serializable
data class Recipes2 (@PrimaryKey val id:Int, val name:String, val ingredients:ArrayList<Ingredients2>, val steps:ArrayList<Steps2>, val servings:Int, var image:String) : java.io.Serializable {
    @Transient
    var favorite : Boolean = false
}