package com.example.home.mybakingappone.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM recipes WHERE recentlyviewed = 1")
    List<Recipes2> loadRecentRecipes();

    //SQLite does not have a boolean data type. Room maps it to an INTEGER column, mapping true to 1 and false to 0.
    @Query("SELECT * FROM recipes WHERE favorite = 1")
    List<Recipes2> loadFavoriteRecipes();


    @Query("SELECT * from recipes WHERE id = :id")
    Recipes2 getRecipe(int id);

    @Query("DELETE FROM recipes")
    void deleteAll();

    @Query("SELECT id from recipes WHERE name = :name")
    int getId(String name);

    @Query("UPDATE recipes SET favorite = 0")
    void deleteAllFavorites();

    @Query("UPDATE recipes SET recentlyviewed = 0")
    void deleteAllRecents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipes2 recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipes2 recipe);

    @Delete
    void deleteRecipe(Recipes2 recipe);
}
