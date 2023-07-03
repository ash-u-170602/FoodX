package com.example.foodx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodx.databinding.TrendingItemRecviewBinding
import com.example.foodx.models.CategoryMeals

class TrendingMealAdapter() : RecyclerView.Adapter<TrendingMealAdapter.TrendingMealViewHolder>() {

    lateinit var onItemClick: ((CategoryMeals) -> Unit)

    private var mealList = ArrayList<CategoryMeals>()
    fun setMeal(mealsList: ArrayList<CategoryMeals>) {
        this.mealList = mealsList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingMealViewHolder {
        return TrendingMealViewHolder(
            TrendingItemRecviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrendingMealViewHolder, position: Int) {

        Glide.with(holder.itemView).load(mealList[position].strMealThumb)
            .into(holder.binding.imgTrendingMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class TrendingMealViewHolder(val binding: TrendingItemRecviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}