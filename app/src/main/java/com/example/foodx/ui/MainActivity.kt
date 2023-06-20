package com.example.foodx.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodx.R
import com.example.foodx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}