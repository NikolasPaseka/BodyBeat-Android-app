package cz.mendelu.xpaseka.bodybeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentParkMapBinding

class ParkMapFragment : BaseFragment<FragmentParkMapBinding, ParkMapViewModel>(ParkMapViewModel::class) {
    private val arguments: ParkDetailFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->
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