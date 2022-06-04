package cz.mendelu.xpaseka.bodybeat

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentAddExerciseBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddExerciseFragment : Fragment() {

    private lateinit var binding: FragmentAddExerciseBinding
    private val vm by sharedViewModel<NewPlanViewModel>()

    private val arguments: AddExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExerciseBinding.inflate(inflater, container, false)

        if (arguments.id != -1L) {
            fillLayout()
            setHasOptionsMenu(true)
        }

        binding.addExerciseButton.setOnClickListener {
            saveExercise()
        }

        return binding.root
    }

    private fun fillLayout() {
        val exercise = vm.exercises[arguments.id.toInt()]
        binding.exerciseTitle.text = exercise.title
        binding.repeats.text = exercise.repeats.toString()
        binding.sets.text = exercise.sets.toString()

        binding.addExerciseButton.text = getString(R.string.edit_exercise)
    }

    private fun saveExercise() {
        if (arguments.id != -1L) {
            val exercise = vm.exercises[arguments.id.toInt()]
            exercise.title = binding.exerciseTitle.text
            exercise.repeats = binding.repeats.text.toInt()
            exercise.sets = binding.sets.text.toInt()
            if (vm.isEditing) {
                lifecycleScope.launch {
                    vm.updateExercise(exercise)
                }
            }
        } else {
            val exercise = Exercise(binding.exerciseTitle.text, binding.repeats.text.toInt(), binding.sets.text.toInt())
            vm.exercises.add(exercise)
        }
        findNavController().popBackStack()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_edit_exercise, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteExercise -> {
                deleteExercise()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteExercise() {
        val exercise = vm.exercises[arguments.id.toInt()]
        if (vm.isEditing) {
            lifecycleScope.launch {
                vm.deleteExercise(exercise)
            }.invokeOnCompletion {
                vm.exercises.remove(exercise)
                findNavController().popBackStack()
            }
        } else {
            vm.exercises.remove(exercise)
            findNavController().popBackStack()
        }
    }
}