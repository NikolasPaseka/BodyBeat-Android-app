package cz.mendelu.xpaseka.bodybeat

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import cz.mendelu.xpaseka.bodybeat.databinding.CountdownDialogBinding
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlanProgressBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlanProgressFragment : BaseFragment<FragmentPlanProgressBinding, PlanProgressViewModel>(PlanProgressViewModel::class){

    private val arguments: PlanDetailFragmentArgs by navArgs()

    private lateinit var plan: Plan
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

        plan = WorkoutsDatabase.getDatabase(requireContext()).plansDao().findById(arguments.id)
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
            setUpCountdownDialog(plan.timerExercises)
        } else {
            if (exerciseList.size > 0) {
                currentSet = 1
                switchToNextExercise()
                setUpCountdownDialog(plan.timerSeries)
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

    private fun setUpCountdownDialog(timer: Int) {
        val countdownDialogBinding = CountdownDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(countdownDialogBinding.root)
            .setTitle("Countdown")
        val countdownDialog = dialogBuilder.show()

        val circularProgressBar = countdownDialogBinding.circularProgressBar
        circularProgressBar.apply {
            // Set Progress Max
            progressMax = timer.toFloat()
            progress = 0f
            // or with animation
            setProgressWithAnimation(progress, 1000) // =1s

            progressBarWidth = 7f
        }
        var i = 0
        val countDownTimer = object: CountDownTimer(timer.toLong()*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                countdownDialogBinding.timer.text = "$i"
                i++
                circularProgressBar.setProgressWithAnimation(i.toFloat(), 200)
            }

            override fun onFinish() {
                countdownDialog.dismiss()
            }
        }
        countDownTimer.start()
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