package cz.mendelu.xpaseka.bodybeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentScheduleBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.view.WeekDayPickerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class ScheduleFragment : BaseFragment<FragmentScheduleBinding, ScheduleViewModel>(ScheduleViewModel::class) {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ScheduleAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentScheduleBinding
        get() = FragmentScheduleBinding::inflate

    override fun initViews() {
        initWeekDayPicker()

        layoutManager = LinearLayoutManager(requireContext())
        adapter = ScheduleAdapter()
        binding.scheduleList.layoutManager = layoutManager
        binding.scheduleList.adapter = adapter

        loadDaySchedule("monday")
        binding.mondayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
    }

    override fun onActivityCreated() {

    }

    inner class WeekDayOnClick(private val dayPicker: WeekDayPickerView) : View.OnClickListener {
        override fun onClick(p0: View?) {
            resetWeekDayPicker()
            dayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
            loadDaySchedule(dayPicker.day)
        }
    }

    private fun loadDaySchedule(day: String) {
        lifecycleScope.launch {
            viewModel.getSchedulesByDay(day)
        }.invokeOnCompletion {
            adapter.notifyListChange(0, viewModel.planSchedules.size+1)
        }
    }

    inner class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

        inner class ScheduleViewHolder(val binding: RowPlanListBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
            return ScheduleViewHolder(
                RowPlanListBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
            if (position < viewModel.planSchedules.size) {
                val planSchedule = viewModel.planSchedules.get(position)

                val time = Date(planSchedule.time * 1000)
                val dateFormatter = SimpleDateFormat("HH:mm", Locale.US)
                holder.binding.rowPlanTitle.text =
                    "${planSchedule.title} ${dateFormatter.format(time)}"
            }
        }

        override fun getItemCount(): Int = viewModel.schedule.size

        fun notifyListChange(start: Int, end: Int) {
            notifyItemRangeChanged(start, end)
        }
    }

    private fun initWeekDayPicker() {
        binding.mondayPicker.setOnClickListener(WeekDayOnClick(binding.mondayPicker))
        binding.tuesdayPicker.setOnClickListener(WeekDayOnClick(binding.tuesdayPicker))
        binding.wednesdayPicker.setOnClickListener(WeekDayOnClick(binding.wednesdayPicker))
        binding.thursdayPicker.setOnClickListener(WeekDayOnClick(binding.thursdayPicker))
        binding.fridayPicker.setOnClickListener(WeekDayOnClick(binding.fridayPicker))
        binding.saturdayPicker.setOnClickListener(WeekDayOnClick(binding.saturdayPicker))
        binding.sundayPicker.setOnClickListener(WeekDayOnClick(binding.sundayPicker))
    }

    private fun resetWeekDayPicker() {
        binding.mondayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.tuesdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.wednesdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.thursdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.fridayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.saturdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.sundayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
    }
}