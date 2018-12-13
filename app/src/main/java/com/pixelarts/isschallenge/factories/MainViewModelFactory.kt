package com.pixelarts.isschallenge.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.pixelarts.isschallenge.remote.APIService
import com.pixelarts.isschallenge.ui.MainViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val apiService: APIService) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return if(modelClass.isAssignableFrom(MainViewModel::class.java)) MainViewModel(apiService) as T
        else throw IllegalArgumentException("ViewModel not found")
    }
}