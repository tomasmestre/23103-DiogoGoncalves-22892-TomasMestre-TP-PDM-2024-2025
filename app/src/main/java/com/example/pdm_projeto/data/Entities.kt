package com.example.pdm_projeto.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary key
    val name: String, // The brand or model name of the vehicle
    val details: String, // Description of the vehicle
    val price: Float, // Price of the vehicle
    val state: Boolean, // Whether the vehicle is active or not
)


@Entity
data class Utilizador (
    val nome: String,
    val password: String,
    val email: String,
    val role: Role,
    @PrimaryKey(autoGenerate = true ) val id: Long = 0
)

enum class Role {
    ADMIN,
    USER
}

@Entity(   foreignKeys = [
    ForeignKey(
        entity = Vehicle::class,
        parentColumns = ["id"],
        childColumns = ["idCar"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Utilizador::class,
        parentColumns = ["id"],
        childColumns = ["idUtilizador"],
        onDelete = ForeignKey.CASCADE
    )

])
data class Reserva(
    val dataInicio: Float,
    val dataFim: Float,
    val estado: Estado,
    val idCar: Long,
    val idUtilizador: Long,

    //var review: String? = null,  // Review do usuário
    //var photoUri: String? = null, // URI da foto da review

    @PrimaryKey(autoGenerate = true ) val id: Long = 0
)


//
enum class Estado {
    UTILIZACAO,
    DISPONIVEL
}

//para as reviews
@Entity
data class Review(
    val reservationId: Long,         // ID da reserva associada
    val reviewText: String,          // Texto da review
    val rating: Float,               // Número de estrelas
    val imagePath: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = 0


)


