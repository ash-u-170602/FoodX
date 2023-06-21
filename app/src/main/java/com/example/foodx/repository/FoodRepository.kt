package com.example.foodx.repository

import com.example.foodx.api.RetrofitInstance

class FoodRepository {

    suspend fun getRandomMeal() =
        RetrofitInstance.api.getRandomMeal()

    suspend fun getTrendingMeal(categoryName: String) =
        RetrofitInstance.api.getTrendingMeal(categoryName)

    suspend fun getMealDetails(id: String) =
        RetrofitInstance.api.getMealDetails(id)
}