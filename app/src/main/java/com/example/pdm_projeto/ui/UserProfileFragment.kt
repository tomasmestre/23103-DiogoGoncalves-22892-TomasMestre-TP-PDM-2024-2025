package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Utilizador
import com.example.pdm_projeto.data.UtilizadorDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileFragment : Fragment() {

    private lateinit var utilizadorDAO: UtilizadorDAO
    private var loggedUserId: Utilizador? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the DAO (requires context, so it must happen after onAttach)
        utilizadorDAO = CarDatabase.getInstance(requireContext()).UtilizadorDAO()

        // Fetch the logged-in user inside a coroutine
        /*  lifecycleScope.launch(Dispatchers.IO) {
              loggedUserId = utilizadorDAO.getLoggedUser() // Chamada dentro de uma coroutine
          }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Fetch and display user data
        fetchUserData(view)

        return view
    }

    private fun fetchUserData(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = loggedUserId?.let { utilizadorDAO.getUserById(it.id) } // Usa loggedUserId, já populado com o usuário logado

            // Atualize a UI na thread principal
            withContext(Dispatchers.Main) {
                user?.let {
                    view.findViewById<TextView>(R.id.tvUserName).text = it.nome
                    view.findViewById<TextView>(R.id.tvUserEmail).text = it.email
                }
            }
        }
    }
}
