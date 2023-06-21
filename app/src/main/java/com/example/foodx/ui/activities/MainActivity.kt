package com.example.foodx.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodx.R
import com.example.foodx.databinding.ActivityMainBinding
import com.example.foodx.repository.FoodRepository
import com.example.foodx.ui.FoodViewModel
import com.example.foodx.ui.FoodViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: FoodViewModel
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val foodRepository = FoodRepository()
        val viewModelProviderFactory = FoodViewModelProviderFactory(application, foodRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[FoodViewModel::class.java]

        val bottomNav = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.FoodNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNav.setupWithNavController(navController)

    }
}