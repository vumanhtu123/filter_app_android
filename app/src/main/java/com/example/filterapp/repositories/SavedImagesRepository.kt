package com.example.filterapp.repositories

import android.graphics.Bitmap
import java.io.File

interface SavedImagesRepository {
    suspend fun loadSaveImages(): List<Pair<File, Bitmap>>?
}