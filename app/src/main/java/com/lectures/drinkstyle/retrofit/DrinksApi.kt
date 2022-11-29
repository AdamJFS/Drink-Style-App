package com.lectures.drinkstyle.retrofit

import com.lectures.drinkstyle.pojo.AlcoholicList
import com.lectures.drinkstyle.pojo.DrinksByPopularList
import com.lectures.drinkstyle.pojo.DrinksList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinksApi {

    @GET("random.php")
    fun getRandomDrink():Call<DrinksList>

    @GET("lookup.php")
    fun getDrinksDetails(@Query("i") id:String) : Call<DrinksList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName:String) : Call<DrinksByPopularList>

    @GET("filter.php")
    fun getAlcoholic(@Query("a") alcoholicName: String) : Call<AlcoholicList>

    @GET("search.php")
    fun searchDrink(@Query("s") searchQuery:String) : Call<DrinksList>
}