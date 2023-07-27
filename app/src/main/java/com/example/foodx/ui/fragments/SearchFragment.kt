package com.example.foodx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodx.R
import com.example.foodx.adapters.MealAdapter
import com.example.foodx.databinding.SearchFragmentBinding
import com.example.foodx.models.CategoryMeals
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.activities.MealActivity
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.util.Constants
import com.example.foodx.util.Resource

class SearchFragment : Fragment() {
    private val binding by lazy { SearchFragmentBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        prepareRecyclerView()

        binding.imgSearch.setOnClickListener { searchMeals() }
        binding.edSearchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMeals()
                true
            } else false
        }

        val listOfCategoryMeals: ArrayList<CategoryMeals> = ArrayList()

        viewModel.searchMealLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
//                    TODO("Hide progress bar")
                    if (response.data == null) {
                        Toast.makeText(requireContext(), "Can't find this Meal", Toast.LENGTH_SHORT)
                            .show()
                    }
                    response.data?.let {
                        listOfCategoryMeals.clear()
                        it.forEach { meal ->
                            listOfCategoryMeals.add(
                                CategoryMeals(
                                    meal.idMeal,
                                    meal.strMeal,
                                    meal.strMealThumb
                                )
                            )
                        }

                        searchRecyclerViewAdapter.differ.submitList(listOfCategoryMeals)
                    }
                    loadingStop()
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
        })

        searchRecyclerViewAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(Constants.MEAL_NAME, meal.strMeal)
            intent.putExtra(Constants.MEAL_THUMB, meal.strMealThumb)
            intent.putExtra(Constants.Meal_ID, meal.idMeal)
            startActivity(intent)
        }

        return binding.root
    }

    private fun loadingStop() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun searchMeals() {
        loading()
        val searchQuery = binding.edSearchBox.text.toString().trim()

        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeals(searchQuery)
        } else loadingStop()
    }

    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealAdapter()
        binding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }
}