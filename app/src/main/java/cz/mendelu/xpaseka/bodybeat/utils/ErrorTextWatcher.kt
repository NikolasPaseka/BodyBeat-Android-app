package cz.mendelu.xpaseka.bodybeat.utils

import android.text.Editable
import android.text.TextWatcher
import cz.mendelu.xpaseka.bodybeat.view.TextInputView

class ErrorTextWatcher(private val textInputView: TextInputView) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (!p0.isNullOrEmpty()) {
            textInputView.setError(null)
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}