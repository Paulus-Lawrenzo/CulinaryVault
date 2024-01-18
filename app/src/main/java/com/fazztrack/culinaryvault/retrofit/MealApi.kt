package com.fazztrack.culinaryvault.retrofit

import com.fazztrack.culinaryvault.pojo.CategoryList
import com.fazztrack.culinaryvault.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularFoods(@Query("c") categoryName: String): Call<CategoryList>
}