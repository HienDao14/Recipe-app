package com.example.easyfood.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.db.Repository
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMealViewModel(application: Application): AndroidViewModel(application) {
    private var _mealDetailLiveData = MutableLiveData<List<Meal>>()
    val mealDetailLiveData : LiveData<List<Meal>>
        get() = _mealDetailLiveData

    private var _mealBottomSheetLiveData = MutableLiveData<List<Meal>>()
    val mealBottomSheetLiveData : LiveData<List<Meal>>
        get() = _mealBottomSheetLiveData

    private lateinit var allMeals: LiveData<List<Meal>>
    private lateinit var repository: Repository

    init{
        val mealDao = MealDatabase.getInstance(application).mealDao()
        repository = Repository(mealDao)
        allMeals = repository.mealList
    }

    fun getAllSavedMeal(){
        viewModelScope.launch {  }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteMeal(meal)
            withContext(Dispatchers.Main){

            }
        }
    }

    fun deleteMeal(meal : Meal) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteMeal(meal)
    }

    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                _mealDetailLiveData.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealDetail", t.message.toString())
            }
        })
    }

    fun isMealSavedInDatabase(mealId: String) : Boolean{
        var meal: Meal?=null
        runBlocking(Dispatchers.IO) {
            meal = repository.getMealById(mealId)
        }
        if(meal == null){
            return false
        }
        return true
    }

    fun deleteMealById(mealId: String){
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteMealById(mealId)
                withContext(Dispatchers.Main){

                }
            }
    }

    fun getMealByIdBottomSheet(mealId: String){
        RetrofitInstance.api.getMealDetail(mealId).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                _mealBottomSheetLiveData.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealDetail", t.message.toString())
            }
        })
    }

    fun observeSaveMeal() : LiveData<List<Meal>>{
        return allMeals
    }
}
