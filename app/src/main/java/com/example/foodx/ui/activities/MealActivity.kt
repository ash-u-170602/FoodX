package com.example.foodx.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodx.R
import com.example.foodx.databinding.ActivityMealBinding
import com.example.foodx.util.Constants.Companion.MEAL_Area
import com.example.foodx.util.Constants.Companion.MEAL_CATEGORY
import com.example.foodx.util.Constants.Companion.MEAL_INSTRUCTIONS
import com.example.foodx.util.Constants.Companion.MEAL_NAME
import com.example.foodx.util.Constants.Companion.MEAL_THUMB
import com.example.foodx.util.Constants.Companion.Meal_URL

class MealActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMealBinding.inflate(layoutInflater) }

    private lateinit var strMeal: String
    private lateinit var strMealThumb: String
    private lateinit var strYoutube: String
    private lateinit var strCategory: String
    private lateinit var strInstructions: String
    private lateinit var strArea: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getMealInformationFromIntent()

        Glide.with(this).load(strMealThumb).into(binding.imageMealDetail)
        binding.collapsingToolbar.title = strMeal
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
        binding.tvCategory.text = "Category: $strCategory"
        binding.tvArea.text = "Cuisine: $strArea"
        binding.tvInstructionsSteps.text = formatInstructionString(strInstructions)

        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strYoutube))
            startActivity(intent)
        }

    }

    private fun formatInstructionString(strInstructions: String): String {
        var count = 1
        val lines = strInstructions.split("\r\n")
        val stringBuilder = StringBuilder()

        for (line in lines) {
            stringBuilder.append("$count. $line\n\n")
            count++
        }

        return stringBuilder.toString()
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        strMeal = intent.getStringExtra(MEAL_NAME)!!
        strMealThumb = intent.getStringExtra(MEAL_THUMB)!!
        strYoutube = intent.getStringExtra(Meal_URL)!!
        strCategory = intent.getStringExtra(MEAL_CATEGORY)!!
        strInstructions = intent.getStringExtra(MEAL_INSTRUCTIONS)!!
        strArea = intent.getStringExtra(MEAL_Area)!!
    }
}