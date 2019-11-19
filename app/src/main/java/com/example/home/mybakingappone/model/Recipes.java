//package com.example.home.mybakingappone.model;
//
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//@Entity(tableName = "recipes")
//public class Recipes implements Serializable {
//
//    @PrimaryKey
//    private int id;
//
//    private String name;
//    private ArrayList<Ingredients> ingredients;
//    private ArrayList<Steps> steps;
//    private int servings;
//    private String image;
//    private boolean favorite;
//
//    public Recipes(int id,
//                   String name,
//                   ArrayList<Ingredients> ingredients,
//                   ArrayList<Steps> steps,
//                   int servings,
//                   String image,
//                   boolean favorite) {
//        this.id = id;
//        this.name = name;
//        this.ingredients = ingredients;
//        this.steps = steps;
//        this.servings = servings;
//        this.image = image;
//        this.favorite=favorite;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public ArrayList<Ingredients> getIngredients() {
//        return ingredients;
//    }
//
//    public void setIngredients(ArrayList<Ingredients> ingredients) {
//        this.ingredients = ingredients;
//    }
//
//    public ArrayList<Steps> getSteps() {
//        return steps;
//    }
//
//    public void setSteps(ArrayList<Steps> steps) {
//        this.steps = steps;
//    }
//
//    public int getServings() {
//        return servings;
//    }
//
//    public void setServings(int servings) {
//        this.servings = servings;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public void setFavorite(boolean favorite){this.favorite=favorite; }
//
//    public boolean getFavorite(){return favorite; }
//
//    @Override
//    public String toString() {
//        return super.toString();
//    }
//}
