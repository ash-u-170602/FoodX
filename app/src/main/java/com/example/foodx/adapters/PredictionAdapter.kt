package com.example.foodx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodx.databinding.PredictionItemrvBinding
import org.tensorflow.lite.support.label.Category

class PredictionAdapter : RecyclerView.Adapter<PredictionAdapter.PredictionAdapterViewHolder> {

    inner class PredictionAdapterViewHolder(val binding: PredictionItemrvBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var predictionList = ArrayList<Category>()

    fun setPredictionList(predictionList: List<Category>) {
        this.predictionList = predictionList as ArrayList<Category>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionAdapterViewHolder {
        return PredictionAdapterViewHolder(
            PredictionItemrvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PredictionAdapterViewHolder, position: Int) {
        val meal = predictionList[position]
        holder.binding.meal.text = meal.label.toString()
        holder.binding
    }

    override fun getItemCount(): Int {
        return predictionList.size
    }
}