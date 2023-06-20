package com.example.foodx.repository

import com.example.foodx.api.RetrofitInstance

class FoodRepository {

    suspend fun getRandomMeal() =
        RetrofitInstance.api.getRandomMeal()

}