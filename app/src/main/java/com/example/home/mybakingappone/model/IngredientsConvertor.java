package com.example.home.mybakingappone.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class IngredientsConvertor {
    @TypeConverter
    public static ArrayList<Ingredients2> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Ingredients2>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Ingredients2> ingredients) {
        Gson gson = new Gson();
        String json = gson.toJson(ingredients);
        return json;
    }
}
