package com.example.easyfood.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.navigation.navArgument
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapter.CategoryMealsAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragment.HomeFragment
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryMealsBinding
    private val categoryMealsViewModel: CategoryMealsViewModel by viewModels()
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecyclerView()

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.mealsLiveData.observe(this, Observer {mealsList ->
            binding.tvCategoryCount.text = mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)
        })

        onItemClick()
    }

    private fun onItemClick() {
        categoryMealsAdapter.setOnCategoryMealClick(object : CategoryMealsAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: MealsByCategory) {
                val intent = Intent(this@CategoryMealsActivity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                    putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                    putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                }
                startActivity(intent)
            }
        })
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeal.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}