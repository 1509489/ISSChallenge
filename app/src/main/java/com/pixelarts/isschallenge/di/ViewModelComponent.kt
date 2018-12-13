package com.pixelarts.isschallenge.di

import com.pixelarts.isschallenge.factories.MainViewModelFactory
import com.pixelarts.isschallenge.ui.MainActivity
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ViewModelComponent {
    fun inject(mainActivity: MainActivity)
    fun getMainViewModelFactory(): MainViewModelFactory
}