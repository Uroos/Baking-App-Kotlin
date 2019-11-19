//package com.example.home.mybakingappone.model;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.OnConflictStrategy;
//import android.arch.persistence.room.Query;
//import android.arch.persistence.room.Update;
//
//import java.util.List;
//
//@Dao
//public interface TaskDao {
//    @Query("SELECT * FROM recipes")
//    List<Recipes> loadAllRecipes();
//
//    @Query("SELECT * from recipes WHERE id = :id")
//    Recipes getRecipe(long id);
//
//    @Insert
//    void insertRecipe(Recipes recipe);
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateRecipe(Recipes recipe);
//
//    @Delete
//    void deleteRecipe(Recipes recipe);
//}
