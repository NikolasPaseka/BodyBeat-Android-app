package cz.mendelu.xpaseka.bodybeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentManageExercisesBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowExerciseListBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ManageExercisesFragment : Fragment() {

    private lateinit var binding: FragmentManageExercisesBinding
    private val vm by sharedViewModel<NewPlanViewModel>()

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ManageExercisesFragment.ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageExercisesBinding.inflate(inflater, container, false)

        val recyclerView = binding.exerciseList
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ExercisesAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        vm.exerciseList
            .observe(viewLifecycleOwner) { t ->
                val callback = TaskDiffUtils(vm.exercises, t!!)
                val result = DiffUtil.calculateDiff(callback)
                result.dispatchUpdatesTo(adapter)

                vm.exercises.clear()
                vm.exercises.addAll(t)
            }

        binding.fab.setOnClickListener {
            findNavController().navigate(ManageExercisesFragmentDirections.actionMananageExercicesFragmentToAddExerciseFragment())
        }

        return binding.root
    }

    inner class ExercisesAdapter : RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

        inner class ExerciseViewHolder(val binding: RowExerciseListBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            return ExerciseViewHolder(
                RowExerciseListBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
            val exercise = vm.exercises.get(position)
            holder.binding.exerciseName.text = exercise.title
            holder.binding.exerciseNumbers.text = "${exercise.sets} x ${exercise.repeats}"
        }

        override fun getItemCount(): Int = vm.exercises.size
    }

    inner class TaskDiffUtils(private val oldList: MutableList<Exercise>, private val newList: MutableList<Exercise>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].title == newList[newItemPosition].title
        }

    }
}