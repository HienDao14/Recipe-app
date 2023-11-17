package com.example.easyfood.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.fragment.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.DetailMealViewModel
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMealBinding
    private lateinit var mealViewModel: DetailMealViewModel
    private var mealId = ""
    private var mealStr = ""
    private var mealThumb = ""
    private var ytUrl = ""
    private lateinit var dtMeal: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mealViewModel = ViewModelProviders.of(this)[DetailMealViewModel::class.java]
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading()

        getMealInfoFromIntent()
        setUpViewWithMealInformation()
        setFloatingButtonStatus()

        mealViewModel.getMealById(mealId)

        mealViewModel.mealDetailLiveData.observe(this
        ) { t ->
            setTextsInView(t!![0])
            stopLoading()
        }

        binding.imgYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ytUrl)))
        }
        binding.fbFavorite.setOnClickListener {
            if(isMealSavedInDatabase()){
                deleteMeal()
                binding.fbFavorite.setImageResource(R.drawable.ic_save)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal was deleted",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                saveMeal()
                binding.fbFavorite.setImageResource(R.drawable.ic_saved)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal saved",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveMeal() {
        mealViewModel.insertMeal(dtMeal)
    }

    private fun deleteMeal() {
        mealViewModel.deleteMealById(mealId)
    }

    private fun setTextsInView(meal: Meal){
        this.dtMeal = meal
        ytUrl = meal.strYoutube
        binding.apply {
            tvInstructions.text = "- Instructions: "
            tvInstructionDetail.text = meal.strInstructions
            tvArea.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            tvArea.text = tvArea.text.toString() + meal.strArea
            tvCategory.text = tvCategory.text.toString() + meal.strCategory
            imgYoutube.visibility = View.GONE
        }
    }

    private fun setFloatingButtonStatus() {
        if(isMealSavedInDatabase()){
            binding.fbFavorite.setImageResource(R.drawable.ic_saved)
        }
        else {
            binding.fbFavorite.setImageResource(R.drawable.ic_save)
        }
    }

    private fun isMealSavedInDatabase(): Boolean{
        return mealViewModel.isMealSavedInDatabase(mealId)
    }

    private fun setUpViewWithMealInformation() {
        binding.apply {
            collapsingToolbar.title = mealStr
            Glide.with(applicationContext)
                .load(mealThumb)
                .into(imgMealDetail)
        }
    }

    private fun getMealInfoFromIntent() {
        val tempIntent = intent

        this.mealId = tempIntent.getStringExtra(HomeFragment.MEAL_ID)!!
        this.mealStr = tempIntent.getStringExtra(HomeFragment.MEAL_NAME)!!
        this.mealThumb= tempIntent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.fbFavorite.visibility = View.GONE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun stopLoading(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.fbFavorite.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}