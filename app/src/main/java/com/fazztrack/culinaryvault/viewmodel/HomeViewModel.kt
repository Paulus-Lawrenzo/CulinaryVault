package com.fazztrack.culinaryvault.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fazztrack.culinaryvault.db.MealDatabase
import com.fazztrack.culinaryvault.pojo.Category
import com.fazztrack.culinaryvault.pojo.CategoryList
import com.fazztrack.culinaryvault.pojo.MealsByCategoryList
import com.fazztrack.culinaryvault.pojo.MealsByCategory
import com.fazztrack.culinaryvault.pojo.Meal
import com.fazztrack.culinaryvault.pojo.MealList
import com.fazztrack.culinaryvault.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularFoodsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var searchMealsLiveData = MutableLiveData<List<Meal>?>()
    private var handleDataNotFound = MutableLiveData<Boolean>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
//                    Log.d("TEST", "meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}")
                }
                else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home Fragment", t.message.toString())
            }
        })
    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let {
                    searchMealsLiveData.postValue(it)
                }
//                if (mealsList.isNullOrEmpty()) {
//                    // handle empty state
//                    handleDataNotFound.postValue(true)
//                } else {
//                    searchMealsLiveData.postValue(mealsList)
//                    handleDataNotFound.postValue(false)
//                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        }
    )

    fun observeSearchMealsLiveData() : LiveData<List<Meal>?> = searchMealsLiveData

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun getPopularFoods() {
        RetrofitInstance.api.getPopularFoods("Seafood").enqueue(object: Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null) {
                    popularFoodsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun observePopularFoodsLiveData(): LiveData<List<MealsByCategory>> {
        return popularFoodsLiveData
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object: Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}