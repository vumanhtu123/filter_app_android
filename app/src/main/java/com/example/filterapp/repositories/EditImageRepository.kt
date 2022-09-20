package com.example.filterapp.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.example.filterapp.data.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
    suspend fun saveImageFilteredImage(filteredBimap: Bitmap): Uri?
}