package cz.mendelu.xpaseka.bodybeat.view

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import cz.mendelu.xpaseka.bodybeat.R
import cz.mendelu.xpaseka.bodybeat.databinding.ViewWeekDayPickerBinding

class WeekDayPickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = 0
) : FrameLayout(context, attrs, defStyleAttrs)
{
    init {
        init(context, attrs)
    }

    private lateinit var binding: ViewWeekDayPickerBinding

    lateinit var day: String

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = ViewWeekDayPickerBinding.inflate(LayoutInflater.from(context), this, true)
        if (attrs != null) {
            loadAttributes(attrs)
        }
    }

    private fun loadAttributes(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.WeekDayPickerView)
        val dayAttr = attributes.getString(R.styleable.WeekDayPickerView_day)
        val labelAttr = attributes.getString(R.styleable.WeekDayPickerView_label)
        if (dayAttr != null) {
            day = dayAttr
        }
        binding.dayText.text = labelAttr
        attributes.recycle()
    }

    fun selectDay(): String {
        binding.dayText.text = "vyser si"
        return day
    }

}