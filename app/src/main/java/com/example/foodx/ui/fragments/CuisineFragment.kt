package com.example.foodx.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodx.R
import com.example.foodx.adapters.CuisinesAdapter
import com.example.foodx.databinding.CuisineFragmentBinding
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.util.Resource

class CuisineFragment : BaseFragment() {
    private val binding by lazy { CuisineFragmentBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel
    private lateinit var cuisinesAdapter: CuisinesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationVisibility(true)
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

        viewModel.cuisinesLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
//                    TODO("Hide progress bar")
                    response.data?.let { listOfCuisines ->
                        cuisinesAdapter.setCuisineList(listOfCuisines)
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


        cuisinesAdapter.onItemClick = { cuisine ->
            viewModel.setArea(cuisine.strArea)
            findNavController().navigate(R.id.action_categoriesFragment_to_cuisineMealFragment)
        }

    }

    private fun prepareRecyclerView() {
        cuisinesAdapter = CuisinesAdapter()
        binding.rvCuisines.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = cuisinesAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navigationVisibility(false)
    }

    override fun onResume() {
        super.onResume()
        navigationVisibility(true)
    }

}