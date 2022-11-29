package com.lectures.drinkstyle.dataBase


import androidx.room.*
import com.lectures.drinkstyle.activites.MainActivity
import com.lectures.drinkstyle.pojo.Drink

@Database(entities = [Drink::class], version = 1)
@TypeConverters(DrinkTypeConvertor::class)
abstract class DrinkDataBase : RoomDatabase() {
    abstract fun drinkDao():DrinkDao

    companion object {
        @Volatile
        var INSTANCE:DrinkDataBase? = null

        @Synchronized
        fun getInstance(context: MainActivity):DrinkDataBase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    DrinkDataBase::class.java,
                    "drink.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as DrinkDataBase
        }
    }
}