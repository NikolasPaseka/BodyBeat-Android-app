package cz.mendelu.xpaseka.bodybeat.fragments.parks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cz.mendelu.xpaseka.bodybeat.architecture.BaseFragment
import cz.mendelu.xpaseka.bodybeat.databinding.FragmentParksBinding
import cz.mendelu.xpaseka.bodybeat.databinding.RowParkListBinding
import cz.mendelu.xpaseka.bodybeat.model.Park
import cz.mendelu.xpaseka.bodybeat.model.Plan
import retrofit2.Call
import retrofit2.Response

class ParksFragment : BaseFragment<FragmentParksBinding, ParksViewModel>(ParksViewModel::class) {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ParksAdapter

    override val bindingInflater: (LayoutInflater) -> FragmentParksBinding
        get() = FragmentParksBinding::inflate

    override fun initViews() {
        binding.fab.setOnClickListener {
            val directions = ParksFragmentDirections.actionParksFragmentToUploadParkFragment()
            findNavController().navigate(directions)
        }

        getAllData()
    }

    private fun getAllData() {
        viewModel.retrofitService.getAllData().enqueue(object: retrofit2.Callback<List<Park>> {
            override fun onResponse(call: Call<List<Park>>, response: Response<List<Park>>) {
                if (response.isSuccessful) {
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
            val park = viewModel.parks[position]
            holder.binding.parkName.text = park.name
            Glide.with(holder.binding.root).load(park.image).into(holder.binding.parkImage)

            holder.binding.root.setOnClickListener {
                val directions = ParksFragmentDirections.actionParksFragmentToParkDetailFragment()
                directions.name = park.name
                directions.image = park.image
                directions.latitude = park.latitude.toFloat()
                directions.longitude = park.longitude.toFloat()
                findNavController().navigate(directions)
            }
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