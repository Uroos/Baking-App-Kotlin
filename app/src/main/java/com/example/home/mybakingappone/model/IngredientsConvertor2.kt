package com.example.home.mybakingappone.model

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IngredientsConvertor2 {

    @TypeConverter
    fun fromString(value: String): ArrayList<Ingredients2> {
        val listType = object : TypeToken<ArrayList<Ingredients2>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(ingredients: ArrayList<Ingredients2>): String = Gson().toJson(ingredients)

}