package com.example.home.mybakingappone.model

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StepsConvertor2 {

        @TypeConverter
        fun fromString(value: String): ArrayList<Steps2> {
            val listType = object : TypeToken<ArrayList<Steps2>>() {}.type
            return Gson().fromJson(value, listType)
            // val turnsType = object : TypeToken<List<Turns>>() {}.type
            // val turns = Gson().fromJson<List<Turns>>(pref.turns, turnsType)
        }

        @TypeConverter
        fun fromArrayList(steps: ArrayList<Steps2>): String = Gson().toJson(steps)
}