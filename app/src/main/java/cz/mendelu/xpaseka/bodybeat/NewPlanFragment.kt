package cz.mendelu.xpaseka.bodybeat

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.databinding.DialogAddScheduleBinding
import cz.mendelu.xpaseka.bodybeat.databinding.DialogTimerBinding
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentNewPlanBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.Schedule
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewPlanFragment : Fragment() {

    private lateinit var binding: FragmentNewPlanBinding
    private val vm by sharedViewModel<NewPlanViewModel>()

    private val arguments: PlanDetailFragmentArgs by navArgs()

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ScheduleAdapter

    enum class TimerDialogType {
        EXERCISE, SERIES
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentNewPlanBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = ScheduleAdapter()
        binding.scheduleList.layoutManager = layoutManager
        binding.scheduleList.adapter = adapter

        binding.exerciseTimerButton.setOnClickListener {
            setUpTimerDialog(TimerDialogType.EXERCISE)
        }
        binding.seriesTimerButton.setOnClickListener {
            setUpTimerDialog(TimerDialogType.SERIES)
        }

        binding.manageExercisesButton.setOnClickListener {
            findNavController().navigate(NewPlanFragmentDirections.actionNewPlanFragmentToManageExercisesFragment())
        }

        binding.addSchedule.setOnClickListener {
            setUpAddScheduleDialog()
        }

        binding.fabSavePlan.setOnClickListener {
            savePlan()
        }

        fillLayout()

        return binding.root
    }

    private fun savePlan() {
        vm.plan.title = binding.workoutPlanTitle.text

        lifecycleScope.launch {
            if (arguments.id == -1L) {
                vm.insertPlan()
            } else {
                vm.updatePlan()
            }
            vm.exercises.forEach { e ->
                if (e.id == null) {
                    e.planId = vm.planId
                    vm.insertExercise(e)
                }
            }
            vm.schedules.forEach { s ->
                if (s.id == null) {
                    s.planId = vm.planId
                    vm.insertSchedule(s)
                }
            }
        }.invokeOnCompletion {
            clearViewModel()
            findNavController().popBackStack()
        }
    }

    private fun fillLayout() {
        if (arguments.id != -1L) {
            lifecycleScope.launch {
                vm.getData(arguments.id)
            }.invokeOnCompletion {
                binding.workoutPlanTitle.text = vm.plan.title
                Log.i("exe", vm.exercises.toString())
                Log.i("sche", vm.schedules.toString())
                if (vm.schedules.size > 0) {
                    adapter.notifyListChange(0, vm.schedules.size+1)
                }
                fillUpTimers()
            }
        } else {
            fillUpTimers()
        }
    }

    private fun setUpTimerDialog(dialogType: TimerDialogType) {
        val timerDialogBinding = DialogTimerBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(timerDialogBinding.root)
            .setCancelable(true)
        val timerDialog = dialogBuilder.show()
        timerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (dialogType == TimerDialogType.EXERCISE) {
            timerDialogBinding.timePickers.fillOutTimer(vm.plan.timerExercises)
        } else {
            timerDialogBinding.timePickers.fillOutTimer(vm.plan.timerSeries)
        }

        timerDialogBinding.saveButton.setOnClickListener {
            if (dialogType == TimerDialogType.EXERCISE) {
                vm.plan.timerExercises = timerDialogBinding.timePickers.timeInSeconds
                binding.exerciseTimerButton.text = getTimerText(vm.plan.timerExercises)
            } else {
                vm.plan.timerSeries = timerDialogBinding.timePickers.timeInSeconds
                binding.seriesTimerButton.text = getTimerText(vm.plan.timerSeries)
            }
            timerDialog.dismiss()
        }

        timerDialogBinding.closeButton.setOnClickListener {
            timerDialog.dismiss()
        }
    }

    private fun fillUpTimers() {
        binding.exerciseTimerButton.text = getTimerText(vm.plan.timerExercises)
        binding.seriesTimerButton.text = getTimerText(vm.plan.timerSeries)
    }

    private fun getTimerText(value: Int): String {
        val seconds: String = if (value % 60 < 10) {
            "0${value % 60}"
        } else {
            "${value % 60}"
        }
        return "${value / 60}:${seconds}"
    }

    private fun setUpAddScheduleDialog() {
        val dialogBinding = DialogAddScheduleBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
        val timerDialog = dialogBuilder.show()
        timerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val spinner = dialogBinding.dayPicker
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.days_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        dialogBinding.timePicker.setIs24HourView(true)

        dialogBinding.saveButton.setOnClickListener {

            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = dialogBinding.timePicker.hour
            calendar[Calendar.MINUTE] = dialogBinding.timePicker.minute

            val timestamp: Long = calendar.timeInMillis
            val day: String = spinner.selectedItem.toString().lowercase()
            vm.schedules.add(Schedule(day, timestamp))

            adapter.notifyListChange(0, vm.schedules.size+1)
            timerDialog.dismiss()
        }

        dialogBinding.closeButton.setOnClickListener {
            timerDialog.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                setUpCancelDialog()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpCancelDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        builder.setTitle(R.string.cancel_dialog_title)
        builder.setMessage(R.string.cancel_dialog_sentence)
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            clearViewModel()
            findNavController().popBackStack()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }

        builder.show()
    }

    private fun clearViewModel() {
        vm.plan = Plan("", 0, 0)
        vm.planId = null
        vm.exercises.clear()
        vm.schedules.clear()
//        if (vm.exerciseList.value != null) {
//            vm.exerciseList.value!!.clear()
//        }
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
            if (position < vm.schedules.size) {
                val schedule = vm.schedules.get(position)
                val time = Date(schedule.time)
                val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                holder.binding.rowPlanTitle.text = "${schedule.day} ${dateFormatter.format(time)}"
            }
        }

        override fun getItemCount(): Int = vm.schedules.size

        fun notifyListChange(start: Int, end: Int) {
            notifyItemRangeChanged(start, end)
        }
    }
}