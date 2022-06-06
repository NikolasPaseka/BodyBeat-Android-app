package cz.mendelu.xpaseka.bodybeat.fragments.parks


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import cz.mendelu.xpaseka.bodybeat.R
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentSelectLocationMapBinding

class SelectLocationMapFragment : BaseFragment<FragmentSelectLocationMapBinding, SelectLocationViewModel>(
    SelectLocationViewModel::class) {

    override val bindingInflater: (LayoutInflater) -> FragmentSelectLocationMapBinding
        get() = FragmentSelectLocationMapBinding::inflate

    override fun initViews() {
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated() {

    }

    private val callback = OnMapReadyCallback { googleMap ->
        var position = LatLng(49.185044, 16.638204)

        val zoomLevel = 10f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))

        googleMap.setOnMapClickListener {
            googleMap.clear()

            viewModel.selectedLocation = it
            googleMap.addMarker(MarkerOptions().position(it).draggable(true))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                if (viewModel.selectedLocation != null) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("latitude", viewModel.selectedLocation!!.latitude)
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("longitude", viewModel.selectedLocation!!.longitude)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.you_have_not_choosen_any_location), Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}