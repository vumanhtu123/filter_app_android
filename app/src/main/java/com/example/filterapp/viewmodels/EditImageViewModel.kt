package com.example.filterapp.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filterapp.data.ImageFilter
import com.example.filterapp.repositories.EditImageRepository
import com.example.filterapp.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {
    //region :: prepare image preview
    private val imagePreviewDataState = MutableLiveData<imagePreviewDataSate>()
    val imagePreviewUiState: LiveData<imagePreviewDataSate> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.io {
            runCatching {
                emiImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null) {
                    emiImagePreviewUiState(bitmap = bitmap)
                } else {
                    emiImagePreviewUiState(error = "Unable image preview")
                }
            }.onFailure {
                emiImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emiImagePreviewUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dateSate = imagePreviewDataSate(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dateSate)
    }


    data class imagePreviewDataSate(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )

    //endregion
    //region:: Load image filters
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataSate>()
    val imageFiltersUiSate : LiveData<ImageFiltersDataSate> get() = imageFiltersDataState

    fun loadImageFilters(originalImage: Bitmap){
        Coroutines.io {
            runCatching {
                emitImageFiltersUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFiltersUiState(imageFilters = imageFilters)
            }.onFailure {
                emitImageFiltersUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage : Bitmap): Bitmap{
        return runCatching {
            val previewWith = 150
            val previewHeight = originalImage.height * previewWith / originalImage.width
            Bitmap.createScaledBitmap(originalImage,previewWith,previewHeight,false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFiltersUiState(
        isLoading: Boolean = false,
        imageFilters: List<ImageFilter>? = null,
        error: String? = null
    ){
        val dataState = ImageFiltersDataSate(isLoading,imageFilters,error)
        imageFiltersDataState.postValue(dataState)
    }

    data class ImageFiltersDataSate(
        val isLoading: Boolean,
        val imageFilters: List<ImageFilter>?,
        val error: String?
    )
    //endregion
    //region Save Filtered image
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBimap : Bitmap){
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepository.saveImageFilteredImage(filteredBimap)
            }.onSuccess { saveImageUri ->
                emitSaveFilteredImageUiState(uri = saveImageUri)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading,uri,error)
        saveFilteredImageDataState.postValue(dataState)
    }
    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )
    //endregion
}