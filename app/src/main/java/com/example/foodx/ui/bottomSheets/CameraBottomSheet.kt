package com.example.foodx.ui.bottomSheets

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.navigation.fragment.findNavController
import com.example.foodx.R
import com.example.foodx.databinding.CameraBottomSheetBinding
import com.example.foodx.ml.LiteModelAiyVisionClassifierFoodV11
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.viewModels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.tensorflow.lite.support.image.TensorImage

class CameraBottomSheet : BottomSheetDialogFragment() {
    private val binding by lazy { CameraBottomSheetBinding.inflate(layoutInflater) }

    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        binding.camera.setOnClickListener {

        }


        binding.files.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 10)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10) {
            if (data != null) {
                val uri = data.data
                try {

                    //Getting bitmap of the selected image's uri
                    val bitmap = Media.getBitmap(requireContext().contentResolver, uri)

                    //Open model
                    val model = LiteModelAiyVisionClassifierFoodV11.newInstance(requireContext())

                    // Creates inputs for reference.
                    val image = TensorImage.fromBitmap(bitmap)

                    // Runs model inference and gets result.
                    val outputs = model.process(image)
                    val probability = outputs.probabilityAsCategoryList

                    // Releases model resources if no longer used.
                    model.close()

                    val sortedList = probability.sortedByDescending { it.score }
                    val top5Items = sortedList.take(5)

                    top5Items.forEach {
                        Log.d("lolol", it.toString())
                    }

                    viewModel.setPredictionList(top5Items)

                    findNavController().navigate(R.id.action_homeFragment_to_predictionFragment)
                    dismiss()

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }

}