package com.lectures.drinkstyle.activites
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lectures.drinkstyle.R
import com.lectures.drinkstyle.dataBase.DrinkDataBase
import com.lectures.drinkstyle.viewModel.HomeViewModel
import com.lectures.drinkstyle.viewModel.HomeViewModelFactory


class MainActivity : AppCompatActivity() {
    val viewModel: HomeViewModel by lazy {
        val drinkDataBase = DrinkDataBase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(drinkDataBase)
        ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}