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
import cz.mendelu.xpaseka.bodybeat.databinding.RowSheduleListInfoBinding
import cz.mendelu.xpaseka.bodybeat.model.Schedule
import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog
import cz.mendelu.xpaseka.bodybeat.view.WeekDayPickerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class ScheduleFragment : BaseFragment<FragmentScheduleBinding, ScheduleViewModel>(ScheduleViewModel::class) {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ScheduleAdapter

    private lateinit var calendarSelection: Calendar

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

        calendarSelection = Calendar.getInstance()
        calculateMonthlyProgression()

        binding.nextMonthButton.setOnClickListener {
            calendarSelection.add(Calendar.MONTH, 1)
            calculateMonthlyProgression()
        }
        binding.previousMonthButton.setOnClickListener {
            calendarSelection.add(Calendar.MONTH, -1)
            calculateMonthlyProgression()
        }
    }

    private fun setSelectedCalendarText() {
        binding.selectedMonth.text = calendarSelection.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        binding.selectedYear.text = calendarSelection.get(Calendar.YEAR).toString()
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
        val oldList: MutableList<ScheduleViewModel.PlanSchedule> = mutableListOf()
        oldList.addAll(viewModel.planSchedules)

        lifecycleScope.launch {
            viewModel.getSchedulesByDay(day)
        }.invokeOnCompletion {
            val callback = TaskDiffUtils(oldList, viewModel.planSchedules)
            val result = DiffUtil.calculateDiff(callback)
            result.dispatchUpdatesTo(adapter)
        }
    }

    private fun calculateMonthlyProgression() {
        val daysInMonth = mutableMapOf(
            Calendar.MONDAY to 0,
            Calendar.TUESDAY to 0,
            Calendar.WEDNESDAY to 0,
            Calendar.THURSDAY to 0,
            Calendar.FRIDAY to 0,
            Calendar.SATURDAY to 0,
            Calendar.SUNDAY to 0
        )

        val calendar = calendarSelection
        val currentMonth = calendar.get(Calendar.MONTH)
        for (day: Int in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            calendar.set(calendar.get(Calendar.YEAR), currentMonth, day)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            daysInMonth[dayOfWeek] = daysInMonth[dayOfWeek]!! + 1
        }

        var numberOfWorkoutsInMonth = 0
        var schedules: MutableList<Schedule> = mutableListOf()
        lifecycleScope.launch {
            schedules = viewModel.getSchedules()
        }.invokeOnCompletion {
            for (schedule in schedules) {
                val day = convertDay(schedule.day)
                numberOfWorkoutsInMonth += daysInMonth[day]!!
            }

            var scheduleLog: MutableList<ScheduleLog>
            lifecycleScope.launch {
                scheduleLog = viewModel.getSchedulesLog()
                val cal = Calendar.getInstance()
                val filteredLog = scheduleLog.filter { schedule ->
                    cal.timeInMillis = schedule.date
                    cal.get(Calendar.YEAR) == calendarSelection.get(Calendar.YEAR)
                            && cal.get(Calendar.MONTH) == calendarSelection.get(Calendar.MONTH)
                }
                var completion: Float = 0f
                if (numberOfWorkoutsInMonth != 0) {
                    completion = filteredLog.size.toFloat() / numberOfWorkoutsInMonth
                }
                if (completion > 1.0f) {
                    completion = 1.0f
                }
                completion *= 100
                binding.number.text = completion.roundToInt().toString() + " %"
                val circularProgressBar = binding.circularProgressBar
                circularProgressBar.apply {
                    progressMax = 100f
                    progress = completion
                    setProgressWithAnimation(progress, 1000)

                    progressBarWidth = 10f
                }

                setSelectedCalendarText()
            }
        }
    }

    private fun convertDay(day: String): Int {
        return when (day) {
            "sunday" -> 1
            "monday" -> 2
            "tuesday" -> 3
            "wednesday" -> 4
            "thursday" -> 5
            "friday" -> 6
            "saturday" -> 7
            else -> 1
        }
    }

    inner class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

        inner class ScheduleViewHolder(val binding: RowSheduleListInfoBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
            return ScheduleViewHolder(
                RowSheduleListInfoBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
            if (position < viewModel.planSchedules.size) {
                val planSchedule = viewModel.planSchedules[position]

                val time = Date(planSchedule.time)
                val dateFormatter = SimpleDateFormat("HH:mm", Locale.US)
                holder.binding.rowScheduleTitle.text = planSchedule.title
                holder.binding.rowScheduleTime.text = dateFormatter.format(time)
            }
        }

        override fun getItemCount(): Int = viewModel.planSchedules.size
    }

    inner class TaskDiffUtils(private val oldList: MutableList<ScheduleViewModel.PlanSchedule>, private val newList: MutableList<ScheduleViewModel.PlanSchedule>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].title == newList[newItemPosition].title
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].title == newList[newItemPosition].title
                    && oldList[oldItemPosition].time == newList[newItemPosition].time
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
        binding.mondayPicker.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle_transparent)
        binding.mondayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.tuesdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.wednesdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.thursdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.fridayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.saturdayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
        binding.sundayPicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_background))
    }
}