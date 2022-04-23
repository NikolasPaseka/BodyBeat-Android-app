package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlanProgressBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise

class PlanProgressFragment : BaseFragment<FragmentPlanProgressBinding, PlanProgressViewModel>(PlanProgressViewModel::class){

    private val arguments: PlanDetailFragmentArgs by navArgs()

    private var exerciseList: MutableList<Exercise> = mutableListOf()
    private lateinit var currentExercise: Exercise
    private var currentSet = 1

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ExercisesAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentPlanProgressBinding
        get() = FragmentPlanProgressBinding::inflate

    override fun initViews() {
        val recyclerView = binding.upcomingExercisesList
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ExercisesAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        exerciseList = WorkoutsDatabase.getDatabase(requireContext()).plansDao().getExercisesTesting(arguments.id)

        if (exerciseList.size > 0) {
            switchToNextExercise()
        }

        binding.exerciseDoneButton.setOnClickListener {
            exerciseDone()
        }
    }

    override fun onActivityCreated() {

    }

    private fun exerciseDone() {
        if (currentSet < currentExercise.sets) {
            currentSet++
            changeSetsLabel()
        } else {
            if (exerciseList.size > 0) {
                currentSet = 1
                switchToNextExercise()
            } else {
                binding.currentExerciseTitle.text = "All done congrats"
            }
        }
    }

    private fun switchToNextExercise() {
        currentExercise = adapter.deleteItem(0)
        binding.currentExerciseTitle.text = currentExercise.title
        changeSetsLabel()
    }

    private fun changeSetsLabel() {
        binding.currentSetTitle.text = currentSet.toString() + "/" + currentExercise.sets
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

        fun deleteItem(index: Int): Exercise{
            val exercise = exerciseList.removeAt(index)
            notifyDataSetChanged()
            return exercise
        }

        override fun getItemCount(): Int = exerciseList.size
    }
}