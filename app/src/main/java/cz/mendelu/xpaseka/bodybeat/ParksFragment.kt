package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentParksBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowParkListBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowPlanListBinding
import cz.mendelu.xpaseka.bodybeat.model.Park
import cz.mendelu.xpaseka.bodybeat.model.Plan
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ParksFragment : BaseFragment<FragmentParksBinding, ParksViewModel>(ParksViewModel::class) {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ParksAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentParksBinding
        get() = FragmentParksBinding::inflate

    override fun initViews() {
//        val recyclerView = binding.parksList
//        layoutManager = LinearLayoutManager(requireContext())
//        adapter = ParksAdapter()
//        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter
        getAllData()
    }

    private fun getAllData() {
        viewModel.retrofitService.getAllData().enqueue(object: retrofit2.Callback<List<Park>> {
            override fun onResponse(call: Call<List<Park>>, response: Response<List<Park>>) {
                if (response.isSuccessful) {
                    Log.i("parky", response.body().toString())
                    viewModel.parks = response.body() as MutableList<Park>
                    val recyclerView = binding.parksList
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ParksAdapter()
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Park>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    override fun onActivityCreated() {

    }

    inner class ParksAdapter : RecyclerView.Adapter<ParksAdapter.ParkViewHolder>() {

        inner class ParkViewHolder(val binding: RowParkListBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkViewHolder {
            return ParkViewHolder(
                RowParkListBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ParkViewHolder, position: Int) {
            val park = viewModel.parks.get(position)
            holder.binding.parkName.text = park.name
            Glide.with(holder.binding.root).load(park.image).into(holder.binding.parkImage)

            // nastaveni sudy radku na jinou barvu, musi se resit recyklace - vyresit elsem
//            if (position % 2 == 0) {
//                //holder.binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_lighter))
//                holder.binding.rowPlanTitleCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_lighter))
//            } else {
//                holder.binding.rowPlanTitleCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_darker))
//            }

//            holder.binding.root.setOnClickListener {
//                val id: Long = plan.id!!
//                val directions = PlansFragmentDirections.actionPlansFragmentToPlanDetailFragment()
//                directions.id = id
//                findNavController().navigate(directions)
//            }
            // textcolor
            // holder.binding.personName.setTextColor(textColor)
        }

        override fun getItemCount(): Int = viewModel.parks.size
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