package com.example.foodx.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodx.R
import com.example.foodx.databinding.ActivityMealBinding
import com.example.foodx.util.Constants.Companion.MEAL_NAME
import com.example.foodx.util.Constants.Companion.MEAL_THUMB
import com.example.foodx.util.Constants.Companion.Meal_ID

class MealActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMealBinding.inflate(layoutInflater) }

    private lateinit var strMeal: String
    private lateinit var strMealThumb: String
    private lateinit var mealId: String
    private lateinit var strCategory: String
    private lateinit var strArea: String
    private lateinit var strInstructions: String
    private lateinit var strYoutube: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getMealInformationFromIntent()




        Glide.with(this).load(strMealThumb).into(binding.imageMealDetail)
        binding.apply {
            collapsingToolbar.title = strMeal
            collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
            collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
            tvCategory.text = "Category: $strCategory"
            tvArea.text = "Cuisine: $strArea"
            tvInstructionsSteps.text = formatInstructionString(strInstructions)

            imgYoutube.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strYoutube))
                startActivity(intent)
            }
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
        mealId = intent.getStringExtra(Meal_ID)!!
    }
}