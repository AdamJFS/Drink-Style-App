package com.lectures.drinkstyle.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lectures.drinkstyle.dataBase.DrinkDataBase


class DrinkViewModelFactory(
    private val drinkDataBase: DrinkDataBase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DrinkViewModel(drinkDataBase) as T
    }
}