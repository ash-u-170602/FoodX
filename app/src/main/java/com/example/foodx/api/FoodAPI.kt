package com.example.foodx.api

import com.example.foodx.models.CategoriesResponse
import com.example.foodx.models.CategoryList
import com.example.foodx.models.CategoryMeals
import com.example.foodx.models.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodAPI {

    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealResponse>

    @GET("lookup.php?")
    suspend fun getMealDetails(@Query("i")id: String) : Response<MealResponse>

    @GET("filter.php?")
    suspend fun getTrendingMeal(@Query("c") categoryName: String): Response<CategoriesResponse>

    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryList>

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c")categoryName: String): Response<CategoriesResponse>
}