package com.example.pdm_projeto.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UtilizadorDAO {

    // Regsito
    @Insert
    suspend fun insert(user: Utilizador)

    //Get Nome para o LOG IN
    @Query("SELECT * FROM Utilizador WHERE nome = :username")
    suspend fun get(username: String): Utilizador?


    //Get Email para o LOG IN
    @Query("SELECT * FROM utilizador WHERE email = :email")
    suspend fun getByEmail(email: String): Utilizador?

    @Insert
    fun insertAll(users: List<Utilizador>)

    @Query("SELECT * FROM Utilizador WHERE id = :userId LIMIT 1")
    suspend fun getLoggedUser(userId: Long): Utilizador?

    @Query("SELECT * FROM Utilizador")
    fun getAll(): LiveData<List<Utilizador>>


    @Query("SELECT * FROM Utilizador WHERE id = :userId")
    suspend fun getUserById(userId: Long): Utilizador?

}
