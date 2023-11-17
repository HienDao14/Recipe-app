package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.MealsByCategorylist
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel: ViewModel() {

    private val _mealsLiveData = MutableLiveData<List<MealsByCategory>>()
    val mealsLiveData : LiveData<List<MealsByCategory>>
        get() = _mealsLiveData

    fun getMealsByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object: Callback<MealsByCategorylist>{
            override fun onResponse(
                call: Call<MealsByCategorylist>,
                response: Response<MealsByCategorylist>
            ) {
                if(response.body() != null){
                    _mealsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategorylist>, t: Throwable) {
                Log.e("CategoryMealsActivity", t.message.toString())
            }
        })
    }
}