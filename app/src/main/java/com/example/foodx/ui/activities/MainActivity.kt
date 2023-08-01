package com.example.foodx.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodx.R
import com.example.foodx.databinding.ActivityMainBinding
import com.example.foodx.db.MealDatabase
import com.example.foodx.repository.FoodRepository
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.ui.viewModels.HomeViewModelProviderFactory


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: HomeViewModel
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val foodRepository = FoodRepository(MealDatabase(this))
        val viewModelProviderFactory = HomeViewModelProviderFactory(application, foodRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[HomeViewModel::class.java]
        val bottomNav = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.FoodNavHost) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
        navigationVisibility(true)
    }

    fun navigationVisibility(isVisible: Boolean) {
        binding.apply {
            bottomNav.clearAnimation()
            if (isVisible) {
                bottomNav.animate()
                    .translationY(0.0f)
                    .alpha(1.0f)
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            super.onAnimationStart(animation)
                            bottomNav.visibility = View.VISIBLE
                        }
                    })
            } else {
                bottomNav.animate()
                    .translationY(bottomNav.height.toFloat())
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            bottomNav.visibility = View.GONE
                        }
                    })
            }
        }
    }

}