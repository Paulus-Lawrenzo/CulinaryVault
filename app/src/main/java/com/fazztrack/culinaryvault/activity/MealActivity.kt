package com.fazztrack.culinaryvault.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fazztrack.culinaryvault.R
import com.fazztrack.culinaryvault.databinding.ActivityMealBinding
import com.fazztrack.culinaryvault.db.MealDatabase
import com.fazztrack.culinaryvault.fragment.HomeFragment
import com.fazztrack.culinaryvault.pojo.Meal
import com.fazztrack.culinaryvault.viewmodel.MealViewModel
import com.fazztrack.culinaryvault.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealViewModel: MealViewModel
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInfoFromIntent()
        setInformationInViews()

        loadingCase()

        mealViewModel.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageClick()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnFabAddFavorite.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this, "Meal Saved to Favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.ivYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal?= null
    private fun observerMealDetailsLiveData() {
        mealViewModel.observerMealDeatilsLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponseCase()

                val meal = value
                mealToSave = meal

                binding.tvCategoryOnDetail.text = "Category : ${meal!!.strCategory}"
                binding.tvAreaOnDetail.text = "Area : ${meal.strArea}"
                binding.tvInstructionsStep.text = meal.strInstructions
                youtubeLink = meal.strYoutube.toString()
            }
        })
    }

    private fun setInformationInViews() {
        Glide
            .with(applicationContext)
            .load(mealThumb)
            .into(binding.ivMealDetail)

        binding.ctlCollapsing.title = mealName
        binding.ctlCollapsing.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
        binding.ctlCollapsing.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID) ?: ""
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME) ?: ""
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB) ?: ""
    }

    private fun loadingCase() {
        binding.lpiProgressBar.visibility = View.VISIBLE
        binding.btnFabAddFavorite.visibility = View.INVISIBLE
        binding.tvCategoryOnDetail.visibility = View.INVISIBLE
        binding.tvAreaOnDetail.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.ivYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.lpiProgressBar.visibility = View.INVISIBLE
        binding.btnFabAddFavorite.visibility = View.VISIBLE
        binding.tvCategoryOnDetail.visibility = View.VISIBLE
        binding.tvAreaOnDetail.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.ivYoutube.visibility = View.VISIBLE
    }
}