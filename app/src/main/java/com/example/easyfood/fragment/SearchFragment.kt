package com.example.easyfood.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easyfood.R
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapter.MealsAdapter
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }

        observeSearchedMealsLiveData()

        var searchJob : Job?= null
        binding.edtSearchBox.addTextChangedListener {searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }

        onItemClick()
    }

    private fun onItemClick() {
        searchRecyclerViewAdapter.setOnMealClickListener(object :
            MealsAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: Meal) {
                val intent = Intent(context, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                    putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                    putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                }
                startActivity(intent)
            }
        })
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.searchMealsLiveData.observe(viewLifecycleOwner, Observer {mealsList ->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }


    private fun searchMeals() {
        val searchQuery = binding.edtSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = searchRecyclerViewAdapter
        }
    }
}