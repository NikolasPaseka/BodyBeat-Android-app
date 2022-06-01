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
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.lang.IllegalStateException


class UploadParkFragment : BaseFragment<FragmentUploadParkBinding, UploadParkViewModel>(UploadParkViewModel::class) {
    val REQUEST_CODE = 100

    override val bindingInflater: (LayoutInflater) -> FragmentUploadParkBinding
        get() = FragmentUploadParkBinding::inflate

    override fun initViews() {
        var config: HashMap<String, String> = HashMap()
        config.put("cloud_name", "passy")
        config.put("secure", "true");
//        config.put("api_key", "your API Key")
//        config.put("api_secret", "your API secret")
        try {
            MediaManager.init(requireContext(), config)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }


        binding.parkImage.setOnClickListener {
            openGalleryForImage()
        }

        binding.postDataButton.setOnClickListener {
            uploadToCloudinary()
            uploadToApi()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            binding.parkImage.setImageURI(data?.data) // handle chosen image
        }
    }

    private fun uploadToCloudinary() {
        //val cloudinary = Cloudinary("cloudinary://<your-api-key>:<your-api-secret>@<your-cloud-name>")


        val stream = ByteArrayOutputStream()
        binding.parkImage.drawable.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()

        MediaManager.get().upload(byteArray)
            .unsigned("ztvxcvpx")
            .option("public_id", binding.parkName.text)
            .dispatch()


        Toast.makeText(requireContext(), "Task successful", Toast.LENGTH_SHORT).show()

//        MediaManager.get().upload(filepath).unsigned("ztvxcvpx").callback(object : UploadCallback {
//            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
//                Toast.makeText(requireContext(), "Task successful", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
//
//            }
//
//            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
//
//            }
//
//            override fun onError(requestId: String?, error: ErrorInfo?) {
//
//                Toast.makeText(requireContext(), "Task Not successful"+ error, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onStart(requestId: String?) {
//
//                Toast.makeText(requireContext(), "Start", Toast.LENGTH_SHORT).show()
//            }
//        }).dispatch()
    }

    fun uploadToApi() {
        val call = viewModel.retrofitService.postData(Park(binding.parkName.text, 0.0, 0.0,
            "https://res.cloudinary.com/passy/image/upload/" + binding.parkName.text
        ))
        call.enqueue(object : retrofit2.Callback<Park> {
            override fun onResponse(call: Call<Park>, response: Response<Park>) {
                if (response.isSuccessful) {
                    //TODO toast message
                    finishCurrentFragment()
                }
            }

            override fun onFailure(call: Call<Park>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onActivityCreated() {

    }
}