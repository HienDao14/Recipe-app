package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import com.example.easyfood.pojo.Meal

class Repository(private val mealDao: MealDao) {

    val mealList: LiveData<List<Meal>> = mealDao.getAllMeals()

    suspend fun insertFavoriteMeal(meal: Meal){
        mealDao.upsertMeal(meal)
    }

    suspend fun getMealById(mealId: String): Meal {
        return mealDao.getMealById(mealId)
    }

    suspend fun deleteMealById(mealId: String){
        mealDao.deleteMealById(mealId)
    }

    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
}