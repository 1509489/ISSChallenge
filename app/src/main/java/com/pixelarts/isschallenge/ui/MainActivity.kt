package com.pixelarts.isschallenge.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
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
import com.pixelarts.isschallenge.R
import com.pixelarts.isschallenge.adapter.ISSPassAdapter
import com.pixelarts.isschallenge.common.ALTITUDE
import com.pixelarts.isschallenge.common.NUM_OF_PASSES
import com.pixelarts.isschallenge.common.Utils
import com.pixelarts.isschallenge.databinding.ActivityMainBinding
import com.pixelarts.isschallenge.di.ApplicationModule
import com.pixelarts.isschallenge.di.DaggerViewModelComponent
import com.pixelarts.isschallenge.di.NetworkModule
import com.pixelarts.isschallenge.model.ISSPassData
import kotlinx.android.synthetic.main.dialog_layout.view.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private  val USER_REQUEST_CODE = 99

    private var altitude = ALTITUDE
    private var passes = NUM_OF_PASSES

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
        displayISSPassData()
        binding.setLifecycleOwner(this)

        binding.fab.setOnClickListener {
            createDialog().show()
        }

    }

    private fun createDialog(): Dialog {
        return this.let {
            val builder = AlertDialog.Builder(it)
            val view = layoutInflater.inflate(R.layout.dialog_layout, null)
            builder.setView(view)
                .setPositiveButton("Set") { dialog, _ ->
                    if (view.etAltitude.text!!.isNotEmpty()&& view.etPasses.text!!.isNotEmpty())
                    {
                        altitude = Integer.parseInt(view.etAltitude.text.toString())
                        passes = Integer.parseInt(view.etPasses.text.toString())
                        //dialog.dismiss()
                    }
                    else if (view.etAltitude.text!!.isNotEmpty()){
                        altitude = Integer.parseInt(view.etAltitude.text.toString())
                    }
                    else if (view.etPasses.text!!.isNotEmpty()){
                        passes = Integer.parseInt(view.etPasses.text.toString())
                    }
                    else{
                        Toast.makeText(this, "Fields must not be left blank", Toast.LENGTH_SHORT).show()
                    }
                    Log.d(TAG, view.etAltitude.text.toString())
                    displayISSPassData()
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayISSPassData(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        lastLocation = location
                        mainViewModel.loadISSPasses(lastLocation.latitude, lastLocation.longitude, altitude, passes).observe(this, Observer {
                            issPassDataList.clear()
                            for (i in 0 until it?.response!!.size)
                            {
                                issPassDataList.add(ISSPassData(it.response[i].duration.toString(), it.response[i].risetime.toString()))
                                //Log.d(TAG, "Duration: ${issPassDataList[i].duration}")
                            }

                            binding.tvDisplayRequest.text = "International Space Station passes for: " +
                                    "\nLocation: (${it.request.latitude}, ${it.request.longitude}) " +
                                    "\nDate and Time: ${Utils.timestampToDateTime(it.request.datetime.toLong())}" +
                                    "\nAltitude: ${it.request.altitude} \nNumber of Passes: ${it.request.passes}"

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

        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), USER_REQUEST_CODE)
            } else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), USER_REQUEST_CODE)
            }
            false
        } else{
            true
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
                        displayISSPassData()
                    }
                }
                else{
                    Toast.makeText(this, "Permission Require", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
