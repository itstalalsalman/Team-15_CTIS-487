package com.ctis487.workerjsondatabase.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.team_15_ctis_487.Utils
import com.example.team_15_ctis_487.retrofitmodel.Exercise
import com.example.team_15_ctis_487.retrofitmodel.Meal

@Database(
    entities = [Meal::class, Exercise::class],
    version = 1,
    exportSchema = false
)
abstract class FitnessRoomDatabase: RoomDatabase() {

    abstract fun mealDao(): MealDAO

    abstract fun exerciseDao(): ExerciseDAO

    companion object{
        @Volatile  //it makes that instance to visible to other threads
        private var INSTANCE: FitnessRoomDatabase?=null

        fun getDatabase(context:Context): FitnessRoomDatabase {
            val tempInstance = INSTANCE
            if(tempInstance !=null){
                return  tempInstance
            }
            /*
            everthing in this block protected from concurrent execution by multiple threads.In this block database instance is created
            same database instance will be used. If many instance are used, it will be so expensive
             */
            synchronized(this){
                val  instance =Room.databaseBuilder(context.applicationContext, FitnessRoomDatabase::class.java, Utils.DATABASENAME).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
