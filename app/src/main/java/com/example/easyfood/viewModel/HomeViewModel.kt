package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.MealsByCategorylist
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private var _randomMealLiveData = MutableLiveData<Meal>()
    val observedRandomMealLiveData: LiveData<Meal>
        get() = _randomMealLiveData

    private var _popularItemLiveData = MutableLiveData<List<MealsByCategory>>()
    val popularItemLiveData : LiveData<List<MealsByCategory>>
        get() = _popularItemLiveData

    private var _categoriesLiveData = MutableLiveData<List<Category>>()
    val categoriesLiveData : LiveData<List<Category>>
        get() = _categoriesLiveData

    private var _favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()
    val favoriteMealsLiveData : LiveData<List<Meal>>
        get() = _favoriteMealsLiveData

    private var _bottomSheetMealLiveData = MutableLiveData<Meal>()
    val bottomSheetMealLiveData : LiveData<Meal>
        get() = _bottomSheetMealLiveData

    private var _searchMealsLiveData = MutableLiveData<List<Meal>>()
    val searchMealsLiveData : LiveData<List<Meal>>
        get() = _searchMealsLiveData


    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object: Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let{
                    _bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let{
                    _searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        }
    )


    private var saveStateRandomMeals: Meal?=null
    fun getRandomMeal(){
        saveStateRandomMeals?.let {randomMeal ->
            _randomMealLiveData.postValue(randomMeal)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal : Meal = response.body()!!.meals[0]
                    _randomMealLiveData.value = randomMeal
                    saveStateRandomMeals = randomMeal
                } else {
                    return
                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object: Callback<MealsByCategorylist>{
            override fun onResponse(call: Call<MealsByCategorylist>, response: Response<MealsByCategorylist>) {
                if(response.body() != null){
                    _popularItemLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategorylist>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object: Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if(response.body() != null){
                    _categoriesLiveData.value = response.body()!!.categories
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }
}