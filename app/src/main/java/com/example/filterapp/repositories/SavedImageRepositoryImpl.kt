package com.example.filterapp.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File

class SavedImageRepositoryImpl(private val context: Context) : SavedImagesRepository {
    override suspend fun loadSaveImages(): List<Pair<File, Bitmap>>? {
        val savedImages = ArrayList<Pair<File, Bitmap>>()
        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Save Images"
        )
        dir.listFiles()?.let { data ->
            data.forEach { file ->
                savedImages.add(Pair(file,getPreviewBitmap(file)))
            }
            return savedImages
        } ?: return null
    }

    private fun getPreviewBitmap(file: File): Bitmap {
        val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)
        val with = 150
        val height = ((originalBitmap.height * with) / originalBitmap.width)
        return Bitmap.createScaledBitmap(originalBitmap, with, height, false)
    }
}