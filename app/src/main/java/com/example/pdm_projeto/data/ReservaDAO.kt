package com.example.pdm_projeto.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReservaDAO {

    // Insert a new reservation
    @Insert
    suspend fun insert(reserva: Reserva)

    @Query("SELECT * FROM Reserva WHERE idUtilizador = :userId")
    suspend fun getReservasByUser(userId: Long): List<Reserva>

    // Get reservations by car ID
    @Query("SELECT * FROM Reserva WHERE idCar = :carId")
    suspend fun getReservasByCar(carId: Long): List<Reserva>

    // Check if a car is reserved within a specific time range
    @Query("""
        SELECT * FROM Reserva 
        WHERE idCar = :carId 
        AND ((:startTime BETWEEN dataInicio AND dataFim) OR 
             (:endTime BETWEEN dataInicio AND dataFim) OR 
             (dataInicio BETWEEN :startTime AND :endTime))
    """)
    suspend fun isCarReservedInRange(carId: Long, startTime: Float, endTime: Float): List<Reserva>

    // Get all reservations
    @Query("SELECT * FROM Reserva")
    suspend fun getAllReservas(): List<Reserva>

    // Update a reservation (optional, for future use)
    @Update
    suspend fun updateReserva(reserva: Reserva)

    // Delete a reservation by ID
    @Query("DELETE FROM Reserva WHERE id = :reservaId")
    suspend fun deleteReservaById(reservaId: Long)

}
