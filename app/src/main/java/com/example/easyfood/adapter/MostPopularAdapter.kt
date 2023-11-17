package com.example.easyfood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.pojo.MealsByCategory

class MostPopularAdapter: RecyclerView.Adapter<MostPopularAdapter.MostPopularViewHolder>() {
    lateinit var onItemClick: ((MealsByCategory) -> Unit)
     var onLongItemClick: ((MealsByCategory) -> Unit)?=null
    private var mealsList = ArrayList<MealsByCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMeals(mealsList: ArrayList<MealsByCategory>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    class MostPopularViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        val imgPopularMeal : ImageView = view.findViewById(R.id.img_popular_meal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularViewHolder {
        return MostPopularViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.popular_items, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: MostPopularViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.imgPopularMeal)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }
}