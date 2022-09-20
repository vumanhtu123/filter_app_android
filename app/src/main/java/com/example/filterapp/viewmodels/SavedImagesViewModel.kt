package com.example.filterapp.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filterapp.repositories.SavedImagesRepository
import com.example.filterapp.utilities.Coroutines
import java.io.File

class SavedImagesViewModel(private val savedImagesRepository: SavedImagesRepository) : ViewModel() {

    private val savedImagesDataState = MutableLiveData<SavedImagesDataState>()
    val savedImagesUiState: LiveData<SavedImagesDataState> get() = savedImagesDataState

    fun loadSavedImages(){
        Coroutines.io {
            runCatching {
                emitSavedImageUiState(isLoading = true)
                savedImagesRepository.loadSaveImages()
            }.onSuccess { savedImages ->
                if (savedImages.isNullOrEmpty()){
                    emitSavedImageUiState(error = "No Images")
                }else{
                    emitSavedImageUiState(savedImage = savedImages)
                }
            }.onFailure {
                emitSavedImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSavedImageUiState(
        isLoading: Boolean = false,
        savedImage: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ){
        val dataState = SavedImagesDataState(isLoading,savedImage,error)
        savedImagesDataState.postValue(dataState)
    }

    data class SavedImagesDataState(
        val isLoading : Boolean,
        val savedImage : List<Pair<File, Bitmap>>?,
        val error : String?
    )

}