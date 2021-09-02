package com.glivion.plasticdiary.view.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ActivityMapItemBinding
import com.glivion.plasticdiary.model.explore.Map
import com.glivion.plasticdiary.util.setSystemBarColor
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.viewmodel.ExploreViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapItemActivity : AppCompatActivity() {
    private var mapFragment: SupportMapFragment? = null
    private lateinit var binding: ActivityMapItemBinding
    private val viewModel: ExploreViewModel by viewModels()
    private val mapItems = ArrayList<Map>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarColor(this, android.R.color.transparent)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_item)
        binding.lifecycleOwner = this

        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getAllMapItems()

        viewModel.mapItemsData.observe(this, {
            Timber.e("maps: $it")
            mapItems.clear()
            mapItems.addAll(it)
            mapFragment?.getMapAsync { googleMap ->
                addMarkers(googleMap, it)
            }
        })

        viewModel.userErrors.observe(this, { isError ->
            isError.let { e ->
                Timber.e("initViewModel: ${e.localizedMessage}")
                showErrorMessage(binding.parentLayout, e)
            }
        })
    }

    private fun initViews() { 
        mapFragment = supportFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        binding.apply {
            backBtn.setOnClickListener {
                finish()
            }
        }
    }

    fun addMarkers(googleMap: GoogleMap, places: List<Map>) {
        val bounds = LatLngBounds.builder()
        places.forEach { place ->
            bounds.include(LatLng(place.latitude!!.toDouble(), place.longitude!!.toDouble()))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20,20,5))
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(LatLng(place.latitude!!.toDouble(), place.longitude!!.toDouble()))
            )
        }
    }
}