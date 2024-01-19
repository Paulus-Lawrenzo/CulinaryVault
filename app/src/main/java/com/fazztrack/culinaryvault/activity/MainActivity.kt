package com.fazztrack.culinaryvault.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.fazztrack.culinaryvault.R
import com.fazztrack.culinaryvault.db.MealDatabase
import com.fazztrack.culinaryvault.viewmodel.HomeViewModel
import com.fazztrack.culinaryvault.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btnNav)
        val navController = Navigation.findNavController(this, R.id.hostFragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}