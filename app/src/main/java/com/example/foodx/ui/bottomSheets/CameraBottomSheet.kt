package com.example.foodx.ui.bottomSheets

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.foodx.R
import com.example.foodx.databinding.CameraBottomSheetBinding
import com.example.foodx.ml.LiteModelAiyVisionClassifierFoodV11
import com.example.foodx.ui.activities.MainActivity
import com.example.foodx.ui.viewModels.HomeViewModel
import com.example.foodx.util.Constants.Companion.REQUEST_IMAGE_CAPTURE
import com.example.foodx.util.Constants.Companion.REQUEST_IMAGE_SELECT
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
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        binding.files.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_SELECT)
        }
    }

    private fun openCamera() {
        // For example, you can call a function to open the camera here
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(requireContext(), "Camera app not found", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_SELECT) {
            if (data != null) {
                val uri = data.data
                try {
                    startML(uri)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
//                val uri = data.data
                val imageBitmap = data.extras?.get("data") as Bitmap

                try {
                    val model = LiteModelAiyVisionClassifierFoodV11.newInstance(requireContext())

                    // Creates inputs for reference.
                    val image = TensorImage.fromBitmap(imageBitmap)

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

    private fun startML(uri: Uri?) {
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


        viewModel.setPredictionList(top5Items)

        findNavController().navigate(R.id.action_homeFragment_to_predictionFragment)
        dismiss()
    }


    // Call this function from your Activity's onRequestPermissionsResult method to handle the permission result
    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Camera permission granted, handle the logic to access the camera here
            openCamera()
        } else {
            // Camera permission denied, handle this case (e.g., show a message to the user)
            showToast("Permission Denied")
            showPermissionDeniedDialog()
        }
    }

    private fun checkCameraPermission(): Boolean {
        // Check if the permission is already granted
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        // Request the camera permission using the permission request launcher
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showPermissionDeniedDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Camera Permission Denied")
        builder.setMessage("You have denied the camera permission permanently. To enable the camera permission, go to App Settings.")
        builder.setPositiveButton("App Settings") { _, _ ->
            openAppSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", "com.example.foodx", null)
        startActivity(intent)
    }

}