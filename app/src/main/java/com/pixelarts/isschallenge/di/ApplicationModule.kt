package com.pixelarts.isschallenge.di

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import com.pixelarts.isschallenge.R
import com.pixelarts.isschallenge.databinding.ActivityMainBinding
import com.pixelarts.isschallenge.factories.MainViewModelFactory
import com.pixelarts.isschallenge.ui.MainViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class ApplicationModule(val activity: AppCompatActivity) {

    @Provides
    fun providesMainViewModel(mainViewModelFactory: MainViewModelFactory) =
        ViewModelProviders.of(activity, mainViewModelFactory).get(MainViewModel::class.java)

    @Provides
    fun providesMainActivityBinding() = DataBindingUtil.setContentView<ActivityMainBinding>(activity, R.layout.activity_main)
}