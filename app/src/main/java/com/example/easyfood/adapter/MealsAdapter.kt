package com.example.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.Meal

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoriteMealsViewHolder>() {
    private lateinit var onItemClickListener: OnFavoriteClickListener
    inner class FavoriteMealsViewHolder(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    fun setOnMealClickListener(onFavoriteClickListener: OnFavoriteClickListener){
        this.onItemClickListener = onFavoriteClickListener
    }

    private val diffUtil = object: DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealsViewHolder {
        return FavoriteMealsViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onItemClickListener.onFavoriteClick(meal)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnFavoriteClickListener {
        fun onFavoriteClick(meal: Meal)
    }
}