package com.fazztrack.culinaryvault.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.fazztrack.culinaryvault.R
import com.fazztrack.culinaryvault.adapter.CategoryMealsAdapter
import com.fazztrack.culinaryvault.databinding.ActivityCategoryMealsBinding
import com.fazztrack.culinaryvault.fragment.HomeFragment
import com.fazztrack.culinaryvault.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()
        onMealClick()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            binding.tvCategoryCount.text = "Total Count : ${mealsList.size}"
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }

    private fun onMealClick() {
        categoryMealsAdapter.onMealClick = { meals ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meals.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meals.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meals.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvCategoryMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}