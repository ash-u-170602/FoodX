package com.example.foodx.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodx.R
import com.example.foodx.databinding.ActivityMealBinding
import com.example.foodx.db.MealDatabase
import com.example.foodx.models.CategoryMeals
import com.example.foodx.repository.FoodRepository
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.ui.viewModels.HomeViewModelProviderFactory
import com.example.foodx.util.Constants.Companion.MEAL_NAME
import com.example.foodx.util.Constants.Companion.MEAL_THUMB
import com.example.foodx.util.Constants.Companion.Meal_ID

class MealActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMealBinding.inflate(layoutInflater) }

    private lateinit var strMeal: String
    private lateinit var strMealThumb: String
    private lateinit var mealId: String
    private lateinit var mealToSave: CategoryMeals

    private lateinit var viewModel: HomeViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FoodX)
        setContentView(binding.root)

        val foodRepository = FoodRepository(MealDatabase(this))
        val viewModelProviderFactory = HomeViewModelProviderFactory(application, foodRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[HomeViewModel::class.java]


        getMealInformationFromIntent()

        loading()
        viewModel.getMealDetails(mealId)

        viewModel.mealDetailLiveData.observe(this) { mealResponse ->
            val meal = mealResponse.data
            binding.apply {
                if (meal != null) {
                    loadingEnd()
                    tvCategory.text = "Category: ${meal.strCategory}"
                    tvArea.text = "Cuisine: ${meal.strArea}"
                    tvInstructionsSteps.text = meal.strInstructions
                    imgYoutube.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.strYoutube))
                        startActivity(intent)
                    }
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            viewModel.saveMeal(mealToSave)
            Toast.makeText(this, "${mealToSave.strMeal} saved", Toast.LENGTH_SHORT).show()
        }

        Glide.with(this).load(strMealThumb).into(binding.imageMealDetail)
        binding.apply {
            collapsingToolbar.title = strMeal
            collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
            collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
        }
    }


    private fun getMealInformationFromIntent() {
        val intent = intent
        strMeal = intent.getStringExtra(MEAL_NAME)!!
        strMealThumb = intent.getStringExtra(MEAL_THUMB)!!
        mealId = intent.getStringExtra(Meal_ID)!!
        mealToSave = CategoryMeals(mealId, strMeal, strMealThumb)
    }

    private fun loading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            floatingActionButton.visibility = View.INVISIBLE
            tvInstructions.visibility = View.INVISIBLE
            tvCategory.visibility = View.INVISIBLE
            tvArea.visibility = View.INVISIBLE
            imgYoutube.visibility = View.INVISIBLE
        }
    }

    private fun loadingEnd() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            floatingActionButton.visibility = View.VISIBLE
            tvInstructions.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            tvArea.visibility = View.VISIBLE
            imgYoutube.visibility = View.VISIBLE
        }
    }
}