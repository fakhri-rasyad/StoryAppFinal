package com.d121211017.stroyappsubmission.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.databinding.ActivityMapsBinding
import com.d121211017.stroyappsubmission.viewmodel.MapViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel : MapViewModel
    private val boundsBuilder = LatLngBounds.Builder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = getViewModel(this)

        viewModel.storiesWithLocation.observe(this){
            addManyMarker(it)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun setMapStyle(){
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity) : MapViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, factory)[MapViewModel::class.java]
    }

    private fun addManyMarker(storyList : List<ListStoryItem>){
        storyList.forEach { story ->
                val latLng = LatLng(story.lat, story.lon)
                mMap.addMarker(MarkerOptions().position(latLng).title(story.name).snippet(story.description))
                boundsBuilder.include(latLng)
        }

        val bounds : LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}