package com.example.foodx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodx.databinding.CategoryMealItemBinding
import com.example.foodx.models.CategoryMeals

class CategoryMealAdapter : RecyclerView.Adapter<CategoryMealAdapter.CategoryMealsViewHolder>() {
    inner class CategoryMealsViewHolder(val binding: CategoryMealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var mealsList = ArrayList<CategoryMeals>()
    lateinit var onItemClick:((CategoryMeals) -> Unit)

    fun setMealsList(mealsList: List<CategoryMeals>) {
        this.mealsList = mealsList as ArrayList<CategoryMeals>
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
            CategoryMealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb)
            .into(holder.binding.imageCategoryMeal)
        holder.binding.tvCategoryMealName.text = mealsList[position].strMeal
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}