package com.example.pdm_projeto.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Utilizador
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CarDatabase.getInstance(application) // Passa o Application como contexto
    private val userDao = db.UtilizadorDAO()

    //val users: LiveData<List<Utilizador>> = userDao.getAll()

    //para o login
    suspend fun getUserByEmail(email: String): Utilizador? {
        return userDao.getByEmail(email)
    }

    //para o registo
    fun createUser(user: Utilizador) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }
}
