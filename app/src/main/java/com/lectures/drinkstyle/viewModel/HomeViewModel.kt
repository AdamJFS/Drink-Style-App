package com.lectures.drinkstyle.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lectures.drinkstyle.dataBase.DrinkDataBase
import com.lectures.drinkstyle.pojo.*
import com.lectures.drinkstyle.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(
    private val drinkDataBase: DrinkDataBase
):ViewModel() {

    private var randomDrinkLiveData = MutableLiveData<Drink>()
    private var popularItemsLiveData = MutableLiveData<List<DrinksByPopular>>()
    private var alcoholicLiveData = MutableLiveData<List<DrinkAlcoholic>>()
    private var favoritesDrinksLiveData = drinkDataBase.drinkDao().getAllDrinks()
    private val searchDrinkLiveData = MutableLiveData<List<Drink>>()
    private lateinit var allDrinks:LiveData<List<Drink>>



    private var saveStateRandomDrink : Drink ?=null
    fun getRandomDrink(){
        saveStateRandomDrink?.let { randomDrink->
            randomDrinkLiveData.postValue(randomDrink)
            return
        }
        RetrofitInstance.api.getRandomDrink().enqueue(object : Callback<DrinksList> {
            override fun onResponse(call: Call<DrinksList>, response: Response<DrinksList>) {
                if(response.body() !=null) {
                    val randomDrinks: Drink = response.body()!!.drinks[0]
                    randomDrinkLiveData.value = randomDrinks
                    saveStateRandomDrink = randomDrinks
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<DrinksList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }


    fun getAlcoholicItem() {
        RetrofitInstance.api.getAlcoholic("Alcoholic").enqueue(object : Callback<AlcoholicList>{
            override fun onResponse(call: Call<AlcoholicList>, response: Response<AlcoholicList>) {
                if(response.body()!=null){
                    alcoholicLiveData.value = response.body()!!.drinks
                }
            }

            override fun onFailure(call: Call<AlcoholicList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }


    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Cocktail").enqueue(object : Callback<DrinksByPopularList>{
            override fun onResponse(call: Call<DrinksByPopularList>, response: Response<DrinksByPopularList>) {
                if(response.body() != null){
                    popularItemsLiveData.value = response.body()!!.drinks
                }
            }

            override fun onFailure(call: Call<DrinksByPopularList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun searchDrink(searchQuery: String) = RetrofitInstance.api.searchDrink(searchQuery).enqueue(
        object : Callback<DrinksList>{
            override fun onResponse(call: Call<DrinksList>, response: Response<DrinksList>) {
                val drinksList = response.body()?.drinks
                drinksList?.let {
                    searchDrinkLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<DrinksList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        }
    )

    fun observeSearchDrinkLiveData() : LiveData<List<Drink>> = searchDrinkLiveData

    fun insertDrink(drink: Drink){
        viewModelScope.launch {
            drinkDataBase.drinkDao().upsert(drink)

        }
    }

    fun deleteDrink(drink: Drink){
        viewModelScope.launch {
            drinkDataBase.drinkDao().delete(drink)
        }
    }
    fun observeRandomDrinkLiveData():LiveData<Drink>{
        return randomDrinkLiveData
    }

    fun observePopularItemsLiveData():LiveData<List<DrinksByPopular>>{
        return popularItemsLiveData
    }

    fun observeAlcoholicLiveData():LiveData<List<DrinkAlcoholic>>{
        return alcoholicLiveData
    }


    fun observerFavoritesDrinkLiveData():LiveData<List<Drink>>{
        return favoritesDrinksLiveData
    }

    fun observeSaveMeal(): LiveData<List<Drink>> {
        return allDrinks
    }


}