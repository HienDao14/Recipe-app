package com.example.easyfood.retrofit

import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealsByCategorylist
import com.example.easyfood.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php")
    fun getMealDetail(@Query("i")id:String):Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName : String): Call<MealsByCategorylist>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<MealsByCategorylist>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery: String): Call<MealList>
}