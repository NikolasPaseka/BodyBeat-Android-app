package cz.mendelu.xpaseka.bodybeat

import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentNewPlanBinding
import cz.mendelu.xpaseka.bodybeat.model.Plan
import kotlinx.coroutines.launch

class NewPlanFragment : BaseFragment<FragmentNewPlanBinding, NewPlanViewModel>(NewPlanViewModel::class) {

    override val bindingInflater: (LayoutInflater) -> FragmentNewPlanBinding
        get() = FragmentNewPlanBinding::inflate

    override fun initViews() {
        binding.fabSavePlan.setOnClickListener {
            viewModel.plan.title = binding.workoutPlanTitle.text
            viewModel.plan.timerExercises = binding.exerciseTimerInput.text.toString().toInt()
            viewModel.plan.timerSeries = binding.seriesTimerInput.text.toString().toInt()

            lifecycleScope.launch {
                viewModel.insertPlan()
            }.invokeOnCompletion {
                findNavController().popBackStack()
            }
        }
    }

    override fun onActivityCreated() {

    }

}