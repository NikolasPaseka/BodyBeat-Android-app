package cz.mendelu.xpaseka.bodybeat

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.DialogTimerBinding
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentNewPlanBinding
import cz.mendelu.xpaseka.bodybeat.model.Plan
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewPlanFragment : Fragment() {

    private lateinit var binding: FragmentNewPlanBinding
    private val vm by sharedViewModel<NewPlanViewModel>()

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

        binding.exerciseTimerButton.setOnClickListener {
            setUpTimerDialog(TimerDialogType.EXERCISE)
        }
        binding.seriesTimerButton.setOnClickListener {
            setUpTimerDialog(TimerDialogType.SERIES)
        }

        binding.manageExercisesButton.setOnClickListener {
            findNavController().navigate(NewPlanFragmentDirections.actionNewPlanFragmentToManageExercisesFragment())
        }

        binding.fabSavePlan.setOnClickListener {
            vm.plan.title = binding.workoutPlanTitle.text

            lifecycleScope.launch {
                vm.insertPlan()
                vm.exercises.forEach { e ->
                    e.planId = vm.planId
                    vm.insertExercise(e)
                }
            }.invokeOnCompletion {
                findNavController().popBackStack()
            }
        }

        fillUpTimers()

        return binding.root
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
        var seconds: String = if (value % 60 < 10) {
            "0${value % 60}"
        } else {
            "${value % 60}"
        }
        return "${value / 60}:${seconds}"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                clearViewModel()
                findNavController().popBackStack()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearViewModel() {
        vm.plan = Plan("", 0, 0)
        vm.planId = null
        vm.exercises.clear()
        if (vm.exerciseList.value != null) {
            vm.exerciseList.value!!.clear()
        }
    }
}