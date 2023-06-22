package com.example.foodx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodx.R
import com.example.foodx.adapters.FavouritesMealAdapter
import com.example.foodx.databinding.FavouritesFragmentBinding
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.activities.MealActivity
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.util.Constants
import com.google.android.material.snackbar.Snackbar

class FavouritesFragment : Fragment(R.layout.favourites_fragment) {
    private val binding by lazy { FavouritesFragmentBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel
    private lateinit var favouritesMealAdapter: FavouritesMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
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

        prepareRecyclerView()
        viewModel.favouritesMealLiveData.observe(requireActivity()) { meals ->
            favouritesMealAdapter.differ.submitList(meals)
        }

        favouritesMealAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(Constants.MEAL_NAME, meal.strMeal)
            intent.putExtra(Constants.MEAL_THUMB, meal.strMealThumb)
            intent.putExtra(Constants.Meal_ID, meal.idMeal)
            startActivity(intent)
        }


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favouritesMealAdapter.differ.currentList[position]
                viewModel.deleteMeal(meal)
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.saveMeal(meal)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavourites)

    }

    private fun prepareRecyclerView() {
        favouritesMealAdapter = FavouritesMealAdapter()
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favouritesMealAdapter
        }

    }
}