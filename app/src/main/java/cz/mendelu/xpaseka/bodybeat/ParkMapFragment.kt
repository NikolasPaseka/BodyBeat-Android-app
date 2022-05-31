package cz.mendelu.xpaseka.bodybeat

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentParkMapBinding
import cz.mendelu.xpaseka.bodybeat.model.Park

class ParkMapFragment : BaseFragment<FragmentParkMapBinding, ParkMapViewModel>(ParkMapViewModel::class) {
    private val arguments: ParkDetailFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val position = LatLng(viewModel.latitude, viewModel.longitude)
        val markerOptions: MarkerOptions = MarkerOptions().position(position).draggable(false)

        googleMap.addMarker(markerOptions)
        val zoomLevel = 13f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override val bindingInflater: (LayoutInflater) -> FragmentParkMapBinding
        get() = FragmentParkMapBinding::inflate

    override fun initViews() {
        viewModel.latitude = arguments.latitude.toDouble()
        viewModel.longitude = arguments.longitude.toDouble()
    }

    override fun onActivityCreated() {

    }
}