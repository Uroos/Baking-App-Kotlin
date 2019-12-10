package com.example.home.mybakingappone.model

import android.arch.persistence.room.*

@Dao
interface TaskDao2 {

    @Query("SELECT * FROM recipes")
    fun loadAllRecipes(): List<Recipes2>

    @Query("SELECT * FROM recipes WHERE recentlyviewed = 1")
    fun loadRecentRecipes(): List<Recipes2>

    //SQLite does not have a boolean data type. Room maps it to an INTEGER column, mapping true to 1 and false to 0.
    @Query("SELECT * FROM recipes WHERE favorite = 1")
    fun loadFavoriteRecipes(): List<Recipes2>

    @Query("SELECT id from recipes WHERE name = :name")
    fun getId(name: String): Int

    @Query("SELECT * from recipes WHERE id = :id")
    fun getRecipe(id: Int): Recipes2

    @Query("DELETE FROM recipes")
     fun deleteAll()

    @Query("UPDATE recipes SET favorite = 0")
     fun deleteAllFavorites()

    @Query("UPDATE recipes SET recentlyviewed = 0")
     fun deleteAllRecents()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertRecipe(recipe: Recipes2);

    @Update(onConflict = OnConflictStrategy.REPLACE)
     fun updateRecipe(recipe: Recipes2);

    @Delete
     fun deleteRecipe(recipe: Recipes2);
}
