package com.example.foodx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodx.databinding.CategoryItemRecviewBinding
import com.example.foodx.models.Category

class CategoriesAdapter() : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: CategoryItemRecviewBinding) :
        RecyclerView.ViewHolder(binding.root)


    private var categoryList = ArrayList<Category>()
    lateinit var onItemClick: ((Category) -> Unit)

    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemRecviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imageCategory)
        holder.binding.tvCategoryName.text = categoryList[position].strCategory
        holder.itemView.setOnClickListener {
            onItemClick.invoke(categoryList[position])
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


}