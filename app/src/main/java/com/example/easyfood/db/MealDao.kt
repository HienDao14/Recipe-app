package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easyfood.pojo.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM mealInformation WHERE idMeal =:id")
    fun getMealById(id: String) : Meal

    @Query("DELETE FROM mealInformation WHERE idMeal = :id")
    suspend fun deleteMealById(id: String)
}