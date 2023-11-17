package com.example.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealsByCategory

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {
    private var mealsList = ArrayList<MealsByCategory>()
    private lateinit var onFavoriteItemClickListener: CategoryMealsAdapter.OnFavoriteClickListener

    fun setMealsList(mealsList: List<MealsByCategory>){
        this.mealsList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    fun setOnCategoryMealClick(onFavoriteClickListener: OnFavoriteClickListener){
        this.onFavoriteItemClickListener = onFavoriteClickListener
    }

    inner class CategoryMealsViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        val meal = mealsList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgMeal)

        holder.binding.tvMealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onFavoriteItemClickListener.onFavoriteClick(meal)
        }
    }
    interface OnFavoriteClickListener {
        fun onFavoriteClick(meal: MealsByCategory)
    }
}