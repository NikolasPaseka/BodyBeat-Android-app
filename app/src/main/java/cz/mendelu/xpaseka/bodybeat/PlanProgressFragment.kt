package cz.mendelu.xpaseka.bodybeat

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import cz.mendelu.xpaseka.bodybeat.databinding.DialogCountdownBinding
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlanProgressBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan
import kotlinx.coroutines.launch

class PlanProgressFragment : BaseFragment<FragmentPlanProgressBinding, PlanProgressViewModel>(PlanProgressViewModel::class){

    private val arguments: PlanDetailFragmentArgs by navArgs()

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ExercisesAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentPlanProgressBinding
        get() = FragmentPlanProgressBinding::inflate

    override fun initViews() {
        print(arguments.id)

        val recyclerView = binding.upcomingExercisesList
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ExercisesAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.plan = viewModel.getPlan(arguments.id)
        }.invokeOnCompletion {
            //viewModel.exerciseList = WorkoutsDatabase.getDatabase(requireContext()).plansDao().getExercisesTesting(arguments.id)
            viewModel.getExercisesFromPlan(arguments.id)
                .observe(viewLifecycleOwner) { t ->
                    val callback = TaskDiffUtils(viewModel.exerciseList, t!!)
                    val result = DiffUtil.calculateDiff(callback)
                    result.dispatchUpdatesTo(adapter)

                    viewModel.exerciseList.clear()
                    viewModel.exerciseList.addAll(t)
                    if (viewModel.exerciseList.size > 0) {
                        switchToNextExercise()
                    }
                }

            binding.exerciseDoneButton.setOnClickListener {
                exerciseDone()
            }
        }
    }

    override fun onActivityCreated() {

    }

    private fun exerciseDone() {
        if (viewModel.currentExercise != null) {
            if (viewModel.currentSet < viewModel.currentExercise!!.sets) {
                viewModel.currentSet++
                changeSetsLabel()
                setUpCountdownDialog(viewModel.plan.timerExercises)
            } else {
                if (viewModel.exerciseList.size > 0) {
                    viewModel.currentSet = 1
                    switchToNextExercise()
                    setUpCountdownDialog(viewModel.plan.timerSeries)
                } else {
                    binding.currentExerciseTitle.text = "All done congrats"
                }
            }
        }
    }

    private fun switchToNextExercise() {
        viewModel.currentExercise = adapter.deleteItem(0)
        if (viewModel.currentExercise != null) {
            binding.currentExerciseTitle.text = viewModel.currentExercise!!.title
            changeSetsLabel()
        }
    }

    private fun changeSetsLabel() {
        binding.currentSetTitle.text = "${viewModel.currentSet}/${viewModel.currentExercise!!.sets}"
    }

    private fun setUpCountdownDialog(timer: Int) {
        val countdownDialogBinding = DialogCountdownBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(countdownDialogBinding.root)
            .setCancelable(true)
            // change to false in production
        val countdownDialog = dialogBuilder.show()
        countdownDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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
            val exercise = viewModel.exerciseList.get(position)
            holder.binding.rowPlanTitle.text = exercise.title
        }

        fun deleteItem(index: Int): Exercise{
            val exercise = viewModel.exerciseList.removeAt(index)
            notifyDataSetChanged()
            return exercise
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