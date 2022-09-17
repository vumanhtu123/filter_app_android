package com.example.filterapp.dependencyinjection

import com.example.filterapp.viewmodels.EditImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModeModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
}