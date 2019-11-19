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
    @Query("SELECT * FROM recipes")
    List<Recipes2> loadAllRecipes();

    @Query("SELECT * from recipes WHERE id = :id")
    Recipes2 getRecipe(long id);

    @Insert
    void insertRecipe(Recipes2 recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipes2 recipe);

    @Delete
    void deleteRecipe(Recipes2 recipe);
}
