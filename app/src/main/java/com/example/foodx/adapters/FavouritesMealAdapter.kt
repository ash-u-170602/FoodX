package com.example.foodx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodx.databinding.CategoryMealItemBinding
import com.example.foodx.models.CategoryMeals

class FavouritesMealAdapter :
    RecyclerView.Adapter<FavouritesMealAdapter.FavouritesMealAdapterViewHolder>() {

    inner class FavouritesMealAdapterViewHolder(val binding: CategoryMealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    lateinit var onItemClick:((CategoryMeals) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<CategoryMeals>() {
        override fun areItemsTheSame(oldItem: CategoryMeals, newItem: CategoryMeals): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: CategoryMeals, newItem: CategoryMeals): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesMealAdapterViewHolder {
        return FavouritesMealAdapterViewHolder(
            CategoryMealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouritesMealAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imageCategoryMeal)
        holder.binding.tvCategoryMealName.text = meal.strMeal
        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}