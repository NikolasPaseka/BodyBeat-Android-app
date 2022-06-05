package cz.mendelu.xpaseka.bodybeat

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentParkDetailBinding
import cz.mendelu.xpaseka.bodybeat.model.Park

class ParkDetailFragment : BaseFragment<FragmentParkDetailBinding, ParkDetailViewModel>(ParkDetailViewModel::class) {
    private val arguments: ParkDetailFragmentArgs by navArgs()

    override val bindingInflater: (LayoutInflater) -> FragmentParkDetailBinding
        get() = FragmentParkDetailBinding::inflate

    override fun initViews() {
        viewModel.park = Park(
            arguments.name,
            arguments.latitude.toDouble(),
            arguments.longitude.toDouble(),
            arguments.image
        )

        binding.parkName.text = viewModel.park.name
        Glide.with(binding.root).load(viewModel.park.image).into(binding.parkImage)

        binding.showLocationButton.setOnClickListener {
            val directions = ParkDetailFragmentDirections.actionParkDetailFragmentToParkMapFragment()
            directions.latitude = viewModel.park.latitude.toFloat()
            directions.longitude = viewModel.park.longitude.toFloat()
            findNavController().navigate(directions)
        }
    }



    override fun onActivityCreated() {
    }
}