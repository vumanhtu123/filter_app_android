package com.example.filterapp.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filterapp.utilities.Coroutines
import com.example.filterapp.repositories.EditImageRepository

class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {
    private val imagePreviewDataState = MutableLiveData<imagePreviewDataSate>()
    val imagePreviewUiState : LiveData<imagePreviewDataSate> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri){
        Coroutines.io {
            runCatching {
                emiImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null)
                {
                    emiImagePreviewUiState(bitmap = bitmap)
                }else
                {
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
    ){
        val dateSate = imagePreviewDataSate(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dateSate)
    }


    data class imagePreviewDataSate(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )
}