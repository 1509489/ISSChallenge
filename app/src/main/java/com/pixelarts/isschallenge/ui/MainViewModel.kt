package com.pixelarts.isschallenge.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.pixelarts.isschallenge.common.ALTITUDE
import com.pixelarts.isschallenge.common.NUM_OF_PASSES
import com.pixelarts.isschallenge.model.APIResponse
import com.pixelarts.isschallenge.remote.APIService
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel(private val apiService: APIService): ViewModel() {

    /*@Inject
    lateinit var apiService: APIService*/


    /*init {
        loadISSPasses(51.5044812, -0.0586698)
    }*/

    fun loadISSPasses(latitude: Double, longitude: Double, altitude: Int, passes: Int):LiveData<APIResponse>{
        val response = MutableLiveData<APIResponse>()

        apiService.getISSPasses(latitude, longitude, altitude, passes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<APIResponse>{
                override fun onSuccess(t: APIResponse) {
                    response.value = t
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }

            })

        return response
    }
}