package cz.mendelu.xpaseka.bodybeat.fragments.parks

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import cz.mendelu.xpaseka.bodybeat.R
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentUploadParkBinding
import cz.mendelu.xpaseka.bodybeat.utils.ApiUploader
import cz.mendelu.xpaseka.bodybeat.utils.ErrorTextWatcher
import cz.mendelu.xpaseka.bodybeat.utils.HashGenerator
import java.io.ByteArrayOutputStream


class UploadParkFragment : BaseFragment<FragmentUploadParkBinding, UploadParkViewModel>(
    UploadParkViewModel::class) {
    private val requestCode = 100
    private var isImagePicked: Boolean = false

    override val bindingInflater: (LayoutInflater) -> FragmentUploadParkBinding
        get() = FragmentUploadParkBinding::inflate

    override fun initViews() {
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "passy"
        config["secure"] = "true"
//        config.put("api_key", "your API Key")
//        config.put("api_secret", "your API secret")
        try {
            MediaManager.init(requireContext(), config)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        binding.parkName.addTextChangeListener(ErrorTextWatcher(binding.parkName))

        binding.parkImage.setOnClickListener {
            openGalleryForImage()
        }

        binding.selectLocationButton.setOnClickListener {
            findNavController().navigate(UploadParkFragmentDirections.actionUploadParkFragmentToSelectLocationMapFragment())
        }

        binding.postDataButton.setOnClickListener {
            upload()
        }

        fillOutLocation()
        fillOutImage()
    }

    private fun upload() {
        if (isImagePicked && binding.parkName.text.isNotEmpty()) {
            uploadToCloudinary()
            Toast.makeText(requireContext(), getString(R.string.park_uploaded), Toast.LENGTH_SHORT).show()
            finishCurrentFragment()
        } else {
            if (binding.parkName.text.isEmpty()) {
                binding.parkName.setError(getString(R.string.cannot_be_empty))
            }
            if (!isImagePicked) {
                Toast.makeText(requireContext(), getString(R.string.image_is_not_picked), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fillOutLocation() {
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Double>("latitude")
            ?.observe(viewLifecycleOwner) {
                viewModel.latitude = it
                binding.selectLocationButton.text = "Lat: " + String.format("%.2f", it)
            }

        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Double>("longitude")
            ?.observe(viewLifecycleOwner) {
                viewModel.longitude = it
                binding.selectLocationButton.text = binding.selectLocationButton.text.toString() + ", Long: " + String.format("%.2f", it)
            }
    }

    private fun fillOutImage() {
        if (viewModel.selectedImage != null) {
            binding.parkImage.setImageDrawable(viewModel.selectedImage)
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == requestCode){
            binding.parkImage.setImageURI(data?.data) // handle chosen image
            viewModel.selectedImage = binding.parkImage.drawable
            isImagePicked = true
        }
    }

    private fun uploadToCloudinary() {
        val stream = ByteArrayOutputStream()
        binding.parkImage.drawable.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()

        val name = binding.parkName.text
        val latitude = viewModel.latitude
        val longitude = viewModel.longitude

        MediaManager.get().upload(byteArray)
            .unsigned("ztvxcvpx")
            .option("public_id", binding.parkName.text + "_" + HashGenerator.getHash())
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                }

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val url = resultData?.get("url").toString()
                    ApiUploader().uploadToApi(name, url, latitude, longitude)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                }
            })
            .dispatch()
    }

    override fun onActivityCreated() {

    }
}