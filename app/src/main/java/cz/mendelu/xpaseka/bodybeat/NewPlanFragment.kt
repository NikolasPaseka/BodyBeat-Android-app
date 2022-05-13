package cz.mendelu.xpaseka.bodybeat

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.DialogTimerBinding
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentNewPlanBinding
import kotlinx.coroutines.launch

class NewPlanFragment : BaseFragment<FragmentNewPlanBinding, NewPlanViewModel>(NewPlanViewModel::class) {

    enum class TimerDialogType {
        EXERCISE, SERIES
    }

    override val bindingInflater: (LayoutInflater) -> FragmentNewPlanBinding
        get() = FragmentNewPlanBinding::inflate

    override fun initViews() {
        binding.exerciseTimerButton.setOnClickListener {
            setUpTimerDialog(TimerDialogType.EXERCISE)
        }
        binding.seriesTimerButton.setOnClickListener {
            setUpTimerDialog(TimerDialogType.SERIES)
        }


        binding.fabSavePlan.setOnClickListener {
            viewModel.plan.title = binding.workoutPlanTitle.text

            lifecycleScope.launch {
                viewModel.insertPlan()
            }.invokeOnCompletion {
                findNavController().popBackStack()
            }
        }
    }

    private fun setUpTimerDialog(dialogType: TimerDialogType) {
        val timerDialogBinding = DialogTimerBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(timerDialogBinding.root)
            .setCancelable(true)
        val timerDialog = dialogBuilder.show()
        timerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        timerDialogBinding.saveButton.setOnClickListener {
            if (dialogType == TimerDialogType.EXERCISE) {
                viewModel.plan.timerExercises = timerDialogBinding.timePickers.timeInSeconds
                binding.exerciseTimerButton.text = getTimerText(viewModel.plan.timerExercises)
            } else {
                viewModel.plan.timerSeries = timerDialogBinding.timePickers.timeInSeconds
                binding.seriesTimerButton.text = getTimerText(viewModel.plan.timerSeries)
            }
            timerDialog.dismiss()
        }

        timerDialogBinding.closeButton.setOnClickListener {
            timerDialog.dismiss()
        }
    }

    private fun getTimerText(value: Int): String {
        var seconds: String = if (value % 60 < 10) {
            "0${value % 60}"
        } else {
            "${value % 60}"
        }
        return "${value / 60}:${seconds}"
    }

    override fun onActivityCreated() {

    }

}