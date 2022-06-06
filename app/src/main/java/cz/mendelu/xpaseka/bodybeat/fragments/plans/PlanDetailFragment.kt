package cz.mendelu.xpaseka.bodybeat.fragments.plans

import android.view.*
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.R
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlanDetailBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowExerciseListBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import kotlinx.coroutines.launch

class PlanDetailFragment : BaseFragment<FragmentPlanDetailBinding, PlanDetailViewModel>(
    PlanDetailViewModel::class) {

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
        setHasOptionsMenu(true)
    }

    private fun fillLayout() {
        val textView: TextView = binding.planTitle
        textView.text = viewModel.plan.title

        binding.seriesTimerInfo.text = getTimerLabel(viewModel.plan.timerSeries) + " " + getString(R.string.between_series)
        binding.exerciseTimerInfo.text = getTimerLabel(viewModel.plan.timerExercises) + " " + getString(
            R.string.between_exercises
        )

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

    fun getTimerLabel(time: Int): String {
        val minutes = time / 60
        val seconds = time % 60
        return if (seconds >= 10) {
            "${minutes}:${seconds}"
        } else {
            "${minutes}:0${seconds}"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_plan_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.editPlan -> {
                val directions = PlanDetailFragmentDirections.actionPlanDetailFragmentToNewPlanFragment()
                directions.id = viewModel.plan.id!!
                findNavController().navigate(directions)
                return true
            }
            R.id.deletePlan -> {
                lifecycleScope.launch {
                    viewModel.deletePlan()
                }.invokeOnCompletion {
                    finishCurrentFragment()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
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
            val exercise = viewModel.exerciseList[position]
            holder.binding.header.text = exercise.title
            holder.binding.subheader.text = "${exercise.sets} x ${exercise.repeats}"
        }

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