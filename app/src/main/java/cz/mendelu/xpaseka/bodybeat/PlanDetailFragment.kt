package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlanDetailBinding
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlansBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlanDetailFragment : BaseFragment<FragmentPlanDetailBinding, PlanDetailViewModel>(PlanDetailViewModel::class) {

    private val arguments: PlanDetailFragmentArgs by navArgs()

    private var exerciseList: MutableList<Exercise> = mutableListOf()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ExercisesAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentPlanDetailBinding
        get() = FragmentPlanDetailBinding::inflate

    override fun initViews() {
        val id = arguments.id
        val plan = WorkoutsDatabase
            .getDatabase(requireContext())
            .plansDao()
            .findById(id)
        val textView: TextView = binding.planTitle
        textView.text = plan.title

        val recyclerView = binding.exerciseList
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ExercisesAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //exerciseList = WorkoutsDatabase.getDatabase(requireContext()).plansDao().getExercisesFromPlan(id)

        // TODO
        WorkoutsDatabase.getDatabase(requireContext()).plansDao().getExercisesFromPlan(id)
        .observe(viewLifecycleOwner, object : Observer<MutableList<Exercise>> {
            override fun onChanged(t: MutableList<Exercise>?) {
                val callback = TaskDiffUtils(exerciseList, t!!)
                val result = DiffUtil.calculateDiff(callback)
                result.dispatchUpdatesTo(adapter)

                exerciseList.clear()
                exerciseList.addAll(t!!)
            }
        })
//        binding.startWorkoutButton.setOnClickListener {
//            if (exerciseList.size > 0) {
//                adapter.deleteItem(0)
//            }
//        }
    }

    override fun onActivityCreated() {
    }

    inner class ExercisesAdapter : RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

        inner class ExerciseViewHolder(val binding: RowPlanListBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            return ExerciseViewHolder(
                RowPlanListBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
            val exercise = exerciseList.get(position)
            holder.binding.rowPlanTitle.text = exercise.title
        }

//        fun deleteItem(index: Int){
//            exerciseList.removeAt(index)
//            notifyDataSetChanged()
//        }

        override fun getItemCount(): Int = exerciseList.size
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