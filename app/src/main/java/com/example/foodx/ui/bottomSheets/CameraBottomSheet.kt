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
import com.example.foodx.databinding.CameraBottomSheetBinding
import com.example.foodx.ml.LiteModelAiyVisionClassifierFoodV11
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.tensorflow.lite.support.image.TensorImage

class CameraBottomSheet : BottomSheetDialogFragment() {
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