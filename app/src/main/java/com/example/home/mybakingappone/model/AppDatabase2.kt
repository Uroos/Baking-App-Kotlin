package com.example.home.mybakingappone.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

//// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Recipes2::class), version = 7, exportSchema = false)
@TypeConverters(IngredientsConvertor2::class, StepsConvertor2::class)
public abstract class AppDatabase2 : RoomDatabase() {

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase2? = null

        fun getsInstance(context: Context): AppDatabase2 {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase2::class.java,
                        "recipe_database"
                )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
    abstract fun taskDao(): TaskDao2
}

