package com.fazztrack.culinaryvault.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fazztrack.culinaryvault.activity.MealActivity
import com.fazztrack.culinaryvault.databinding.FragmentHomeBinding
import com.fazztrack.culinaryvault.pojo.Meal
import com.fazztrack.culinaryvault.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomsMeal: Meal

    companion object{
        const val MEAL_ID = "com.fazztrack.culinaryvault.fragment.idMeal"
        const val MEAL_NAME = "com.fazztrack.culinaryvault.fragment.nameMeal"
        const val MEAL_THUMB = "com.fazztrack.culinaryvault.fragment.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
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

        homeViewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()
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
        homeViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide
                .with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.ivWhatToEat)

            this.randomsMeal = meal
        }
    }
}