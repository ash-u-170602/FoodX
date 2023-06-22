package com.example.foodx.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodx.models.CategoryMeals

@Database(
    entities = [CategoryMeals::class],
    version = 1
)
abstract class MealDatabase : RoomDatabase() {

    abstract fun getMealDao(): MealDao

    companion object {
        @Volatile
        private var instance: MealDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MealDatabase::class.java,
                "meal.db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}