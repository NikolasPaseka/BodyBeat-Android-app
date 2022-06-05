package cz.mendelu.xpaseka.bodybeat

import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import java.util.*

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(SettingsViewModel::class) {

    override val bindingInflater: (LayoutInflater) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override fun initViews() {
        val spinner = binding.languagePicker
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        setSpinnerValue(spinner)

        binding.applicationVersion.text = binding.applicationVersion.text.toString() + BuildConfig.VERSION_NAME

        binding.applyButton.setOnClickListener {
            val activity: MainActivity = requireActivity() as MainActivity
            lifecycleScope.launch {
                activity.saveToDataStore("language", convertSpinner(spinner))
            }.invokeOnCompletion {
                Toast.makeText(requireContext(), getString(R.string.change_effect), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSpinnerValue(spinner: Spinner) {
        var locale: String? = null
        val activity: MainActivity = requireActivity() as MainActivity
        lifecycleScope.launch {
            locale = activity.readFromDataStore("language")
        }.invokeOnCompletion {
            if (locale != null) {
                when (locale!!) {
                    "en" -> spinner.setSelection(1)
                    "cs" -> spinner.setSelection(2)
                    else -> spinner.setSelection(0)
                }
            }
        }
    }

    private fun convertSpinner(spinner: Spinner): String {
        return when (spinner.selectedItem.toString()) {
            "English" -> "en"
            "Czech" -> "cs"
            else -> "system"
        }
    }

    override fun onActivityCreated() {
    }

}