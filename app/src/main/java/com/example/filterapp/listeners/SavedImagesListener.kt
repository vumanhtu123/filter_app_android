package com.example.filterapp.listeners

import java.io.File

interface SavedImagesListener {
    fun onImageClicked(file: File)
}