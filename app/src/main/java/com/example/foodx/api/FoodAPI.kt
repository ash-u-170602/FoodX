package com.example.foodx.api

import com.example.foodx.models.MealList
import retrofit2.Response
import retrofit2.http.GET

interface FoodAPI {

    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealList>
}