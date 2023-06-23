package com.example.foodx.ui.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodx.databinding.CameraBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CameraBottomSheet: BottomSheetDialogFragment() {
    private val binding by lazy { CameraBottomSheetBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.camera.setOnClickListener {

        }


        binding.files.setOnClickListener {

        }
    }
}