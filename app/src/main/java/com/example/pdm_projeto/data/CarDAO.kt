package com.example.pdm_projeto.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CarDAO {
    @Query("SELECT * FROM vehicle")
    fun getAllCars(): List<Vehicle>

    @Query("SELECT * FROM Vehicle WHERE id = :id")
    suspend fun getCarById(id: Long): Vehicle

    @Update
    suspend fun updateState(vehicle: Vehicle)

    @Insert
    fun insertAll(cars: List<Vehicle>)

    @Update
    suspend fun updateCar(vehicle: Vehicle)
}
