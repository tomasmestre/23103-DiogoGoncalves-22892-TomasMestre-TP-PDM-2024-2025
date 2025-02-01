package com.example.pdm_projeto.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReviewForDao {
    @Insert
    suspend fun insertReview(review: Review)

    @Query("SELECT * FROM review WHERE reservationId = :reservationId")
    suspend fun getReviewsByReservationId(reservationId: Long): List<Review>

    //add
    @Query("SELECT * FROM review")
    suspend fun getAllReviews(): List<Review>
}

/*    // Inserir uma nova review
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    // Obter todas as reviews associadas a uma reserva específica
    @Query("SELECT * FROM review WHERE reservationId = :reservationId")
    suspend fun getReviewsByReservationId(reservationId: Long): List<Review>

    // Atualizar uma review
    @Update
    suspend fun updateReview(review: Review)

    // Deletar uma review
    @Delete
    suspend fun deleteReview(review: Review)*/
