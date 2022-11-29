package com.lectures.drinkstyle.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lectures.drinkstyle.dataBase.DrinkDataBase
import com.lectures.drinkstyle.pojo.Drink
import com.lectures.drinkstyle.pojo.DrinksList
import com.lectures.drinkstyle.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrinkViewModel(
    val drinkDatabase: DrinkDataBase
):ViewModel() {
    private var drinkDetailsLiveData = MutableLiveData<Drink>()


    fun getDrinkDetail(id:String){
        RetrofitInstance.api.getDrinksDetails(id).enqueue(object : Callback<DrinksList> {
            override fun onResponse(call: Call<DrinksList>, response: Response<DrinksList>) {
                if (response.body() != null) {
                    drinkDetailsLiveData.value = response.body()!!.drinks[0]
                }
                else
                    return
            }

            override fun onFailure(call: Call<DrinksList>, t: Throwable) {
                Log.d("DrinkActivity", t.message.toString())
            }
        })
    }

    fun observerDrinkDetailsLiveData():LiveData<Drink>{
        return drinkDetailsLiveData
    }

    fun insertDrink(drink: Drink){
        viewModelScope.launch {
            drinkDatabase.drinkDao().upsert(drink)
        }
    }



}