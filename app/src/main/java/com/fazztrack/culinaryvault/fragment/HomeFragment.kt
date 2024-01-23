package com.fazztrack.culinaryvault.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fazztrack.culinaryvault.R
import com.fazztrack.culinaryvault.activity.CategoryMealsActivity
import com.fazztrack.culinaryvault.activity.MainActivity
import com.fazztrack.culinaryvault.activity.MealActivity
import com.fazztrack.culinaryvault.adapter.CategoriesAdapter
import com.fazztrack.culinaryvault.adapter.MostPopularAdapter
import com.fazztrack.culinaryvault.databinding.FragmentHomeBinding
import com.fazztrack.culinaryvault.pojo.MealsByCategory
import com.fazztrack.culinaryvault.pojo.Meal
import com.fazztrack.culinaryvault.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomsMeal: Meal
    private lateinit var popularFoodsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.fazztrack.culinaryvault.fragment.idMeal"
        const val MEAL_NAME = "com.fazztrack.culinaryvault.fragment.nameMeal"
        const val MEAL_THUMB = "com.fazztrack.culinaryvault.fragment.thumbMeal"
        const val CATEGORY_NAME = "com.fazztrack.culinaryvault.fragment.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        popularFoodsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        preparePopularFoodsRecyclerView()
        viewModel.getPopularFoods()
        observePopularFoodsLiveData()
        onPopularFoodClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onMealsClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularFoodClick() {
        popularFoodsAdapter.onFoodClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularFoodsRecyclerView() {
        binding.rvPopularFoods.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularFoodsAdapter
        }
    }

    private fun observePopularFoodsLiveData() {
        viewModel.observePopularFoodsLiveData().observe(viewLifecycleOwner
        ) { mealsList ->
            popularFoodsAdapter.setMeals(mealsList = mealsList as ArrayList<MealsByCategory>)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        homeViewModel.getRandomMeal()
//    }

    private fun onRandomMealClick() {
        binding.cvWhatToEat.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomsMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomsMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomsMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide
                .with(this@HomeFragment)
                .load(meal?.strMealThumb)
                .into(binding.ivWhatToEat)

            this.randomsMeal = meal
        }
    }
}