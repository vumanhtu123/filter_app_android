package com.example.filterapp.activities.editimage

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.filterapp.activities.main.MainActivity
import com.example.filterapp.databinding.ActivityEditImageBinding
import com.example.filterapp.utilities.displayToast
import com.example.filterapp.utilities.show
import com.example.filterapp.viewmodels.EditImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditImageBinding

    private val viewModel: EditImageViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setUpObservers()
        prepareImagePreview()
    }

    private fun setUpObservers() {
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                binding.imagePreview.setImageBitmap(bitmap)
                binding.imagePreview.show()
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun prepareImagePreview() {
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { image ->
            viewModel.prepareImagePreview(image)
        }
    }

//    private fun displayImagesView() {
//        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { image ->
//            val inputStream = contentResolver.openInputStream(image)
//            val bitmap = BitmapFactory.decodeStream(inputStream)
//            binding.imagePreview.setImageBitmap(bitmap)
//            binding.imagePreview.visibility = View.VISIBLE
//            Log.e("tuvm", "success")
//        }
//    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }
}