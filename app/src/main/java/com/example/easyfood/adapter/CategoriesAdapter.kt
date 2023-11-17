package com.example.easyfood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.CategoryItemBinding
import com.example.easyfood.pojo.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var categoryList = ArrayList<Category>()
    var onItemClick: ((Category) -> Unit) ?= null

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(categoriesList: List<Category>){
        this.categoryList = categoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(var binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val viewInflater = CategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(viewInflater)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategory)
        holder.binding.tvCategory.text = categoryList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(categoryList[position])
        }
    }
}