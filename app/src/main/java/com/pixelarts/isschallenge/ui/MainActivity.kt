package com.pixelarts.isschallenge.ui

import android.Manifest
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pixelarts.isschallenge.adapter.ISSPassAdapter
import com.pixelarts.isschallenge.databinding.ActivityMainBinding
import com.pixelarts.isschallenge.di.ApplicationModule
import com.pixelarts.isschallenge.di.DaggerViewModelComponent
import com.pixelarts.isschallenge.di.NetworkModule
import com.pixelarts.isschallenge.model.APIResponse
import com.pixelarts.isschallenge.model.ISSPassData
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private  val USER_REQUEST_CODE = 99

    @Inject
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation:Location

    private lateinit var issPassDataList: ArrayList<ISSPassData>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: ISSPassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()
        init()
        issPassDataList = ArrayList()
        layoutManager = LinearLayoutManager(this)
        getLocation()
        binding.setLifecycleOwner(this)

    }

    private fun getLocation(){
        var data: LiveData<APIResponse>? = null
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        lastLocation = location
                        mainViewModel.loadISSPasses(lastLocation.latitude, lastLocation.longitude).observe(this, Observer {
                            for (i in 0 until it?.response!!.size)
                            {
                                issPassDataList.add(ISSPassData(it.response[i].duration.toString(), it.response[i].risetime.toString()))
                                //Log.d(TAG, "Duration: ${issPassDataList[i].duration}")
                            }

                            adapter = ISSPassAdapter(issPassDataList)
                            binding.recyclerView.layoutManager = layoutManager
                            binding.recyclerView.adapter = adapter
                        })


                    }
                }

    }

    private fun init(){
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .build().inject(this)
    }

    private fun checkPermission(): Boolean{

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), USER_REQUEST_CODE)
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), USER_REQUEST_CODE)
            }
            return false
        }
        else{
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode)
        {
            USER_REQUEST_CODE ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        getLocation()
                    }
                }
                else{
                    Toast.makeText(this, "Permission Require", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
