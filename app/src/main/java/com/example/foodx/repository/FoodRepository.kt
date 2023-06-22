package com.example.foodx.repository

import com.example.foodx.api.RetrofitInstance
import com.example.foodx.db.MealDatabase
import com.example.foodx.models.CategoryMeals

class FoodRepository(
    val db: MealDatabase
) {

    suspend fun getRandomMeal() =
        RetrofitInstance.api.getRandomMeal()

    suspend fun getTrendingMeal(categoryName: String) =
        RetrofitInstance.api.getTrendingMeal(categoryName)

    suspend fun getCuisineMeal(cuisineName: String) =
        RetrofitInstance.api.getCuisineMeal(cuisineName)

    suspend fun getMealDetails(id: String) =
        RetrofitInstance.api.getMealDetails(id)

    suspend fun getCategories() =
        RetrofitInstance.api.getCategories()

    suspend fun getMealsByCategory(categoryName: String) =
        RetrofitInstance.api.getMealsByCategory(categoryName)

    suspend fun getCuisines() =
        RetrofitInstance.api.getCuisines()



    suspend fun upsert(meal: CategoryMeals) = db.getMealDao().upsert(meal)

    suspend fun deleteMeal(meal: CategoryMeals) = db.getMealDao().deleteMeal(meal)

    fun getSavedMeals() = db.getMealDao().getAllMeals()
}