package com.example.foodx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodx.R
import com.example.foodx.adapters.CategoriesAdapter
import com.example.foodx.adapters.TrendingMealAdapter
import com.example.foodx.databinding.HomeFragmentBinding
import com.example.foodx.models.CategoryMeals
import com.example.foodx.models.Meal
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.activities.MealActivity
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.util.Constants.Companion.MEAL_NAME
import com.example.foodx.util.Constants.Companion.MEAL_THUMB
import com.example.foodx.util.Constants.Companion.Meal_ID
import com.example.foodx.util.Resource

class HomeFragment : Fragment() {
    private val binding by lazy { HomeFragmentBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var trendingMealAdapter: TrendingMealAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter


    override fun onResume() {
        super.onResume()
        viewModel.getRandomMeal()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getRandomMeal()
        }


        trendingMealAdapter = TrendingMealAdapter()
        categoriesAdapter = CategoriesAdapter()


        binding.rvTrendingMeals.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingMealAdapter
        }

        binding.rvCategory.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }

        viewModel.trendingMealLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
//                    TODO("Hide progress bar")
                    response.data?.let { mealList ->
                        trendingMealAdapter.setMeal(mealList as ArrayList<CategoryMeals>)
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
                }
            }
        }


        viewModel.randomMealLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
//                    TODO("Hide progress bar")
                    response.data?.let { mealResponse ->
                        val randomMeal = mealResponse.meals[0]
                        val imageURL = randomMeal.strMealThumb
                        Glide.with(requireContext()).load(imageURL).into(binding.imgRandomMeal)
                        this.randomMeal = randomMeal
                    }
                    binding.swipeToRefresh.isRefreshing = false
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
                }
            }
        }

        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
//                    TODO("Hide progress bar")
                    response.data?.let { categories ->
                        categoriesAdapter.setCategoryList(categories)
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
                }
            }
        }

        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            intent.putExtra(Meal_ID, randomMeal.idMeal)
            startActivity(intent)
        }

        trendingMealAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            intent.putExtra(Meal_ID, meal.idMeal)
            startActivity(intent)
        }

        categoriesAdapter.onItemClick = { category ->
            viewModel.setStrMeal(category.strCategory)
            findNavController().navigate(R.id.action_homeFragment_to_categoryMealsFragment)
        }

        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

    }
}