package com.example.foodx.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodx.R
import com.example.foodx.databinding.CuisineFragmentBinding
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.viewModels.HomeViewModel

class CuisineFragment:Fragment() {
    private val binding by lazy { CuisineFragmentBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel

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
    }

}