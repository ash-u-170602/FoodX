package com.example.foodx.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodx.models.CategoryMeals

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meals: CategoryMeals)

    @Delete
    suspend fun deleteMeal(meals: CategoryMeals)

    @Query("SELECT * FROM meals")
    fun getAllMeals(): LiveData<List<CategoryMeals>>
}