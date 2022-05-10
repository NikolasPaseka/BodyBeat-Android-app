package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentNewPlanBinding
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlansBinding
import cz.mendelu.xpaseka.bodybeat.model.Plan

class NewPlanFragment : Fragment() {

    private var _binding: FragmentNewPlanBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewPlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlanBinding.inflate(inflater, container, false)

        binding.fabSavePlan.setOnClickListener {
            val title = binding.newPlanTitle.text.toString()
            val exerciseTimer: Int = binding.exerciseTimerInput.text.toString().toInt()
            val seriesTimer: Int = binding.seriesTimerInput.text.toString().toInt()

            WorkoutsDatabase
                .getDatabase(requireContext())
                .plansDao()
                .insert(Plan(title, exerciseTimer, seriesTimer))
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewPlanViewModel::class.java)
        // TODO: Use the ViewModel
    }

}