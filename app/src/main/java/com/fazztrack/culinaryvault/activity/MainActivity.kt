package com.fazztrack.culinaryvault.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.fazztrack.culinaryvault.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btnNav)
        val navController = Navigation.findNavController(this, R.id.hostFragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}