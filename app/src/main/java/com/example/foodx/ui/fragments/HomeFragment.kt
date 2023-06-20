package com.example.foodx.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodx.databinding.HomeFragmentBinding
import com.example.foodx.ui.FoodViewModel
import com.example.foodx.ui.MainActivity
import com.example.foodx.util.Resource

class HomeFragment : Fragment() {
    private val binding by lazy { HomeFragmentBinding.inflate(layoutInflater) }

    lateinit var viewModel: FoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as MainActivity).viewModel

        viewModel.randomMeal.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
//                    TODO("Hide progress bar")
                    response.data?.let { mealResponse ->
                        val randomMeal = mealResponse.meals[0]
                        val imageURL = randomMeal.strMealThumb
                        Glide.with(requireContext()).load(imageURL).into(binding.imgRandomMeal)
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


    }
}