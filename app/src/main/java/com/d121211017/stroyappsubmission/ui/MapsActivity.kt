package com.d121211017.stroyappsubmission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.local.datastore
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.d121211017.stroyappsubmission.databinding.ActivityMapsBinding
import com.d121211017.stroyappsubmission.viewmodel.MapViewModel
import com.d121211017.stroyappsubmission.viewmodel.StoryListViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel : MapViewModel
    private lateinit var pref : UserPreferences
    private val boundsBuilder = LatLngBounds.Builder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreferences.getInstance(application.datastore)
        viewModel = getViewModel(this, pref)

        viewModel.storiesWithLocation.observe(this){
            addManyMarker(it)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
    private fun getViewModel(appCompatActivity: AppCompatActivity, pref: UserPreferences) : MapViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application, pref)
        return ViewModelProvider(appCompatActivity, factory)[MapViewModel::class.java]
    }

    private fun addManyMarker(storyList : List<ListStoryItem>){
        storyList.forEach {
            story ->
                val latLng = LatLng(story.lat, story.lon)
                mMap.addMarker(MarkerOptions().position(latLng).title(story.name).contentDescription(story.description))
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