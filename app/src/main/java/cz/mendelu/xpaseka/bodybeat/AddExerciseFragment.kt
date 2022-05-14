package cz.mendelu.xpaseka.bodybeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentAddExerciseBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddExerciseFragment : Fragment() {

    private lateinit var binding: FragmentAddExerciseBinding
    private val vm by sharedViewModel<NewPlanViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExerciseBinding.inflate(inflater, container, false)

        binding.addExerciseButton.setOnClickListener {
            val exercise = Exercise(binding.exerciseTitle.text, binding.repeats.text.toInt(), binding.sets.text.toInt())

            val list: MutableList<Exercise>
            if (vm.exerciseList.value == null) {
                list = mutableListOf()
            } else {
                list = vm.exerciseList.value!!
            }

            list.add(exercise)
            vm.exerciseList.value = list
            findNavController().popBackStack()
        }

        return binding.root
    }
}