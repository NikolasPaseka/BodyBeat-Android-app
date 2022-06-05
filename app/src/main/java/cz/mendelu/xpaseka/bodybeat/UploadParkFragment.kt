package cz.mendelu.xpaseka.bodybeat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.cloudinary.android.MediaManager
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentUploadParkBinding
import cz.mendelu.xpaseka.bodybeat.model.Park
import cz.mendelu.xpaseka.bodybeat.utils.ErrorTextWatcher
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream


class UploadParkFragment : BaseFragment<FragmentUploadParkBinding, UploadParkViewModel>(UploadParkViewModel::class) {
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

        binding.postDataButton.setOnClickListener {
            if (isImagePicked && binding.parkName.text.isNotEmpty()) {
                uploadToCloudinary()
                uploadToApi()
                Toast.makeText(requireContext(), "Park uploaded", Toast.LENGTH_SHORT).show()
                finishCurrentFragment()
            } else {
                if (binding.parkName.text.isEmpty()) {
                    binding.parkName.setError(getString(R.string.cannot_be_empty))
                }
                if (!isImagePicked) {
                    Toast.makeText(requireContext(), "Image is not picked", Toast.LENGTH_SHORT).show()
                }
            }
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
            isImagePicked = true
        }
    }

    private fun uploadToCloudinary() {
        val stream = ByteArrayOutputStream()
        binding.parkImage.drawable.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()

        MediaManager.get().upload(byteArray)
            .unsigned("ztvxcvpx")
            .option("public_id", binding.parkName.text)
            .dispatch()
    }

    private fun uploadToApi() {
        val call = viewModel.retrofitService.postData(Park(binding.parkName.text, 0.0, 0.0,
            "https://res.cloudinary.com/passy/image/upload/" + binding.parkName.text
        ))
        call.enqueue(object : retrofit2.Callback<Park> {
            override fun onResponse(call: Call<Park>, response: Response<Park>) {
            }

            override fun onFailure(call: Call<Park>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onActivityCreated() {

    }
}