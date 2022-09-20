package com.example.filterapp.dependencyinjection

import com.example.filterapp.repositories.EditImageRepository
import com.example.filterapp.repositories.EditImageRepositoryImpl
import com.example.filterapp.repositories.SavedImageRepositoryImpl
import com.example.filterapp.repositories.SavedImagesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository>{EditImageRepositoryImpl(androidContext())}
    factory<SavedImagesRepository>{SavedImageRepositoryImpl(androidContext())}
}