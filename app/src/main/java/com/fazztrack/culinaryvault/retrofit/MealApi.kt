package com.fazztrack.culinaryvault.retrofit

import com.fazztrack.culinaryvault.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>
}