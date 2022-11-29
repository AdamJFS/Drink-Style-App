package com.lectures.drinkstyle.dataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lectures.drinkstyle.pojo.Drink

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(drink: Drink)

    @Delete
    suspend fun delete(drink: Drink)

    @Query("SELECT * FROM drinksInfo")
    fun getAllDrinks():LiveData<List<Drink>>


}