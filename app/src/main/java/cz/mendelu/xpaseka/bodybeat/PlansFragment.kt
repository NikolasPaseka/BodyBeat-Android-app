package cz.mendelu.xpaseka.bodybeat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentPlansBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlansFragment : BaseFragment<FragmentPlansBinding, PlansViewModel>(PlansViewModel::class) {

    private val planList: MutableList<Plan> = mutableListOf()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PlansAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentPlansBinding
        get() = FragmentPlansBinding::inflate

    override fun initViews() {
        val recyclerView = binding.planList
        layoutManager = LinearLayoutManager(requireContext())
        adapter = PlansAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel
            .getAll()
            .observe(viewLifecycleOwner, object : Observer<MutableList<Plan>> {
                override fun onChanged(t: MutableList<Plan>?) {
                    val callback = TaskDiffUtils(planList, t!!)
                    val result = DiffUtil.calculateDiff(callback)
                    result.dispatchUpdatesTo(adapter)

                    planList.clear()
                    planList.addAll(t!!)
                }
            })

        binding.fab.setOnClickListener {
            findNavController().navigate(PlansFragmentDirections.actionPlansFragmentToNewPlanFragment())
        }
    }

    override fun onActivityCreated() {
    }


    inner class PlansAdapter : RecyclerView.Adapter<PlansAdapter.PlanViewHolder>() {

        inner class PlanViewHolder(val binding: RowPlanListBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
            return PlanViewHolder(
                RowPlanListBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
            val plan = planList.get(position)
            holder.binding.rowPlanTitle.text = plan.title

            holder.binding.root.setOnClickListener {
                val id: Long = plan.id!!
                val directions = PlansFragmentDirections.actionPlansFragmentToPlanDetailFragment()
                directions.id = id
                findNavController().navigate(directions)
            }
        }

        override fun getItemCount(): Int = planList.size
    }

    inner class TaskDiffUtils(private val oldList: MutableList<Plan>, private val newList: MutableList<Plan>) : DiffUtil.Callback() {
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