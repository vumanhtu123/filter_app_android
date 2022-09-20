package com.example.filterapp.dependencyinjection

import com.example.filterapp.repositories.SavedImagesRepository
import com.example.filterapp.viewmodels.EditImageViewModel
import com.example.filterapp.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModeModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}