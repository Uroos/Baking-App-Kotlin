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
//public class StepsConvertor {
//    @TypeConverter
//    public static ArrayList<Steps> fromString(String value) {
//        Type listType = new TypeToken<ArrayList<Steps>>() {
//        }.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromArrayList(ArrayList<Steps> steps) {
//        Gson gson = new Gson();
//        String json = gson.toJson(steps);
//        return json;
//    }
//}
