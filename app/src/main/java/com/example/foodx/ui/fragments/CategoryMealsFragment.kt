package com.example.foodx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodx.adapters.CategoryMealAdapter
import com.example.foodx.databinding.CategoryMealFragmentBinding
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.activities.MealActivity
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.util.Constants
import com.example.foodx.util.Resource

class CategoryMealsFragment : BaseFragment() {
    private val binding by lazy { CategoryMealFragmentBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel
    private lateinit var strCategory: String
    private lateinit var categoryMealAdapter: CategoryMealAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navigationVisibility(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        prepareRecyclerView()
        loading()

        viewModel.strMeal.observe(viewLifecycleOwner) {
            strCategory = it
            viewModel.getMealsByCategory(strCategory)
            binding.tvHeading.text = "Category($strCategory)"
        }

        viewModel.mealByCategoryLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { mealList ->
                        categoryMealAdapter.setMealsList(mealList)
                        loadingStop()
                    }
                }

                is Resource.Error -> {
//                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
//                    showProgressBar()
                    loading()
                }
            }
        }

        categoryMealAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(Constants.MEAL_NAME, meal.strMeal)
            intent.putExtra(Constants.MEAL_THUMB, meal.strMealThumb)
            intent.putExtra(Constants.Meal_ID, meal.idMeal)
            startActivity(intent)
        }
    }

    private fun loadingStop() {
        binding.progressBar.visibility = View.GONE
    }

    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun prepareRecyclerView() {
        categoryMealAdapter = CategoryMealAdapter()
        binding.rvCategoryMeal.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }
}