package com.example.filterapp.activities.editimage

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.filterapp.activities.filteredimage.FilteredImageActivity
import com.example.filterapp.activities.main.MainActivity
import com.example.filterapp.apdaters.ImageFiltersAdapter
import com.example.filterapp.data.ImageFilter
import com.example.filterapp.databinding.ActivityEditImageBinding
import com.example.filterapp.listeners.ImageFilterListener
import com.example.filterapp.utilities.displayToast
import com.example.filterapp.utilities.show
import com.example.filterapp.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object {
        const val KEY_FILTERED_IMAGE = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding
    private lateinit var gpuImage: GPUImage

    //Image BitMap
    private lateinit var original: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

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
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                //for the first time "filtered image = original image"
                original = bitmap
                filteredBitmap.value = bitmap
                with(original) {
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }
//                binding.imagePreview.setImageBitmap(bitmap)
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
        viewModel.imageFiltersUiSate.observe(this) {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filterRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
        filteredBitmap.observe(this) { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        }
        viewModel.saveFilteredImageUiState.observe( this) {
            val saveFilteredImageDataState = it ?: return@observe
            if (saveFilteredImageDataState.isLoading) {
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE
            } else {
                binding.savingProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let { savedImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE, savedImageUri)
                    startActivity(filteredImageIntent)
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
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
        binding.imageSave.setOnClickListener{
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }
        //This will show original image when we long click the imageView until we release click,
        //So that we can the difference between original image and filtered image
        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(original)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}