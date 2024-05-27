package com.example.testgos.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.testgos.model.Car

@Database(entities = [Car::class], version = 1)
abstract class MyDatabase : RoomDatabase(){
    abstract fun carDao() : CarDao

    companion object{
        private var instance: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase{
            if (instance == null){
                instance = Room
                    .databaseBuilder(context, MyDatabase::class.java, "testDB")
                    .allowMainThreadQueries()
                    .build()
            }

            return instance as MyDatabase
        }
    }
}

@Dao
interface CarDao{
    @Insert
    fun insertItem(item: Car)

    @Insert
    fun insertAllItems(item: List<Car>)

    @Delete
    fun deleteItem(item: Car)

    @Query("SELECT * FROM Car WHERE state == ('New')")
    fun loadIsNew(): List<Car>

    @Query("SELECT * FROM Car WHERE state == ('InUse')")
    fun loadInUse(): List<Car>

    @Query("SELECT * FROM Car WHERE state == ('Deleted')")
    fun loadIsDeleted(): List<Car>

    @Query("DELETE FROM Car")
    fun deleteAllItems()

    @Update
    fun updateItem(item: Car)

    @Query("SELECT * FROM Car")
    fun getAllCars(): List<Car>

    @Query("SELECT * FROM Car WHERE id == (:id)")

    fun getCar(id: Int): Car
}