package cz.mendelu.xpaseka.bodybeat.fragments.plans

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.mendelu.xpaseka.bodybeat.R
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentAddExerciseBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.utils.ErrorTextWatcher
import kotlinx.coroutines.launch
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

        binding.exerciseTitle.addTextChangeListener(ErrorTextWatcher(binding.exerciseTitle))

        binding.addExerciseButton.setOnClickListener {
            saveExercise()
        }

        return binding.root
    }

    private fun fillLayout() {
        val exercise = vm.exercises[arguments.id.toInt()]
        binding.exerciseTitle.text = exercise.title
        binding.repeats.setText(exercise.repeats.toString())
        binding.sets.setText(exercise.sets.toString())

        binding.addExerciseButton.text = getString(R.string.edit_exercise)
    }

    private fun saveExercise() {
        if (binding.exerciseTitle.text.isNotEmpty()) {
            var repeats = 0
            var sets = 0
            if (binding.repeats.text.toString().isNotEmpty()) { repeats = binding.repeats.text.toString().toInt() }
            if (binding.sets.text.toString().isNotEmpty()) { sets = binding.sets.text.toString().toInt() }

            if (arguments.id != -1L) {
                val exercise = vm.exercises[arguments.id.toInt()]
                exercise.title = binding.exerciseTitle.text
                exercise.repeats = repeats
                exercise.sets = sets
                if (vm.isEditing) {
                    lifecycleScope.launch {
                        vm.updateExercise(exercise)
                    }
                }
            } else {
                val exercise = Exercise(binding.exerciseTitle.text, repeats, sets)
                vm.exercises.add(exercise)
            }
            findNavController().popBackStack()
        } else {
            binding.exerciseTitle.setError(getString(R.string.cannot_be_empty))
        }
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