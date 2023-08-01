package com.example.foodx.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodx.R
import com.example.foodx.adapters.PredictionAdapter
import com.example.foodx.databinding.PredictionFragmentBinding
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.viewModels.HomeViewModel

class PredictionFragment : BaseFragment() {
    private val binding by lazy { PredictionFragmentBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel
    private lateinit var predictionAdapter: PredictionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navigationVisibility(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        prepareRecyclerView()

        predictionAdapter.setPredictionList(viewModel.predictionList)

        predictionAdapter.onItemClick = { meal ->
            val mealName = meal.label
            viewModel.mealNameForSearch = mealName
            findNavController().navigate(R.id.action_predictionFragment_to_searchFragment)
        }
    }

    private fun prepareRecyclerView() {
        predictionAdapter = PredictionAdapter()
        binding.rvPredictions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = predictionAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        navigationVisibility(false)
    }
}