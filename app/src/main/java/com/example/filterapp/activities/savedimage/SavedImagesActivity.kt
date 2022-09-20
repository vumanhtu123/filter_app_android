package com.example.filterapp.activities.savedimage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.filterapp.databinding.ActivitySavedImagesBinding
import com.example.filterapp.utilities.displayToast
import com.example.filterapp.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setupObserver() {
        viewModel.savedImagesUiState.observe(this) {
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImage?.let { saveImages ->
                displayToast("${saveImages.size} image loaded")
                Log.e("tuvm", "${saveImages.size} image loaded")
            } ?: run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }
}