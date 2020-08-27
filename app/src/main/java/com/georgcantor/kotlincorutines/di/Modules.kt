package com.georgcantor.kotlincorutines.di

import com.georgcantor.kotlincorutines.model.remote.ApiClient
import com.georgcantor.kotlincorutines.repository.Repository
import com.georgcantor.kotlincorutines.ui.fragment.news.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { ApiClient.create(get()) }
}

val repositoryModule = module {
    single { Repository(get()) }
}

val viewModelModule = module(override = true) {
    viewModel {
        NewsViewModel(get())
    }
}