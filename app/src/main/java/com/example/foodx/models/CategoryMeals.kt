package com.example.foodx.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class CategoryMeals(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)