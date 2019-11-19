//package com.example.home.mybakingappone.model;
//
//import android.arch.persistence.room.TypeConverter;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//
//public class IngredientsConvertor {
//    @TypeConverter
//    public static ArrayList<Ingredients> fromString(String value) {
//        Type listType = new TypeToken<ArrayList<Ingredients>>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromArrayList(ArrayList<Ingredients> ingredients) {
//        Gson gson = new Gson();
//        String json = gson.toJson(ingredients);
//        return json;
//    }
//}
