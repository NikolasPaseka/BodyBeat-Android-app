package cz.mendelu.xpaseka.bodybeat.view

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import cz.mendelu.xpaseka.bodybeat.R
import cz.mendelu.xpaseka.bodybeat.databinding.ViewTextInputBinding
import cz.mendelu.xpaseka.bodybeat.databinding.ViewTimerPickerBinding

class TimerPickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = 0
) : FrameLayout(context, attrs, defStyleAttrs)
{
    init {
        init(context, attrs)
    }

    private lateinit var binding: ViewTimerPickerBinding

    var timeInSeconds: Int = 0
        get() {
            return binding.minutesPicker.value*60 + binding.secondsPicker.value
        }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = ViewTimerPickerBinding.inflate(LayoutInflater.from(context), this, true)
        binding.minutesPicker.minValue = 0
        binding.minutesPicker.maxValue = 9
        binding.secondsPicker.minValue = 0
        binding.secondsPicker.maxValue = 60
    }
}