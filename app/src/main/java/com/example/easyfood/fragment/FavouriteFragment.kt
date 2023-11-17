package com.example.easyfood.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapter.MealsAdapter
import com.example.easyfood.databinding.FragmentFavouriteBinding
import com.example.easyfood.fragment.HomeFragment.Companion.MEAL_ID
import com.example.easyfood.fragment.HomeFragment.Companion.MEAL_NAME
import com.example.easyfood.fragment.HomeFragment.Companion.MEAL_THUMB
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoriteAdapter: MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()
        onItemClick()
    }

    private fun onItemClick() {
        favoriteAdapter.setOnMealClickListener(object : MealsAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: Meal) {
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(MEAL_ID,meal.idMeal)
                intent.putExtra(MEAL_NAME,meal.strMeal)
                intent.putExtra(MEAL_THUMB,meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun prepareRecyclerView() {
        favoriteAdapter = MealsAdapter()
        binding.rvFavorite.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }

    }

    private fun observeFavorites() {
        viewModel.favoriteMealsLiveData.observe(viewLifecycleOwner, Observer { meals ->
            favoriteAdapter.differ.submitList(meals)
        })
    }

}