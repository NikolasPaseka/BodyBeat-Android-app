package cz.mendelu.xpaseka.bodybeat.fragments.parks

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class SelectLocationViewModel : ViewModel() {

    var selectedLocation: LatLng? = null

}