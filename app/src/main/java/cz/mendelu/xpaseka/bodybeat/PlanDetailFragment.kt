package cz.mendelu.xpaseka.bodybeat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlanDetailBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowExerciseListBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import kotlinx.coroutines.launch

class PlanDetailFragment : BaseFragment<FragmentPlanDetailBinding, PlanDetailViewModel>(PlanDetailViewModel::class) {

    private val arguments: PlanDetailFragmentArgs by navArgs()

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ExercisesAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentPlanDetailBinding
        get() = FragmentPlanDetailBinding::inflate

    override fun initViews() {
        viewModel.id = arguments.id
        lifecycleScope.launch {
            viewModel.plan = viewModel.getPlan(viewModel.id!!)
        }.invokeOnCompletion {
            fillLayout()
        }
    }

    private fun fillLayout() {
        val textView: TextView = binding.planTitle
        textView.text = viewModel.plan.title

        val recyclerView = binding.exerciseList
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ExercisesAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel.getExercisesFromPlan(viewModel.id!!)
            .observe(viewLifecycleOwner) { t ->
                val callback = TaskDiffUtils(viewModel.exerciseList, t!!)
                val result = DiffUtil.calculateDiff(callback)
                result.dispatchUpdatesTo(adapter)

                viewModel.exerciseList.clear()
                viewModel.exerciseList.addAll(t)
            }
        binding.startWorkoutButton.setOnClickListener {
            val directions = PlanDetailFragmentDirections.actionPlanDetailFragmentToPlanProgressFragment()
            directions.id = arguments.id
            findNavController().navigate(directions)
        }
    }

    override fun onActivityCreated() {
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
            val exercise = viewModel.exerciseList.get(position)
            holder.binding.exerciseName.text = exercise.title
            holder.binding.exerciseNumbers.text = "${exercise.sets} x ${exercise.repeats}"
        }

//        fun deleteItem(index: Int){
//            exerciseList.removeAt(index)
//            notifyDataSetChanged()
//        }

        override fun getItemCount(): Int = viewModel.exerciseList.size
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