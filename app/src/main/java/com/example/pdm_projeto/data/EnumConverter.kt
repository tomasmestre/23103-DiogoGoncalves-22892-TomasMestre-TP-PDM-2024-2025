package com.example.pdm_projeto.data

import androidx.room.TypeConverter
import com.example.pdm_projeto.data.Role
import com.example.pdm_projeto.data.Estado

class EnumConverters {

    // Role
    @TypeConverter
    fun fromRole(role: Role): String {
        return role.name
    }

    @TypeConverter
    fun toRole(value: String): Role {
        return Role.valueOf(value)
    }

    // Estado
    @TypeConverter
    fun fromEstado(estado: Estado): String {
        return estado.name
    }

    @TypeConverter
    fun toEstado(value: String): Estado {
        return Estado.valueOf(value)
    }
}
