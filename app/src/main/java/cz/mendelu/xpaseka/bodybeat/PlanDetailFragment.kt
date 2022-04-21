package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase

class PlanDetailFragment : Fragment() {

    private val arguments: PlanDetailFragmentArgs by navArgs()

    private lateinit var viewModel: PlanDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan_detail, container, false)
        val id = arguments.id
        val plan = WorkoutsDatabase
            .getDatabase(requireContext())
            .plansDao()
            .findById(id)
        val textView: TextView = view.findViewById(R.id.planTitle)
        textView.text = plan.title
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlanDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}