package com.example.pdm_projeto.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Reserva
import com.example.pdm_projeto.databinding.FragmentUserReservationsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserReservationsFragment : Fragment() {

    private var _binding: FragmentUserReservationsBinding? = null
    private val binding get() = _binding!!

    //private val reservationAdapter = ReservationAdapter()
    private val reservationAdapter = ReservationAdapter { reservation ->
        // Clique no botão de review
        handleReviewClick(reservation)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserReservationsBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        setupRecyclerView()

        // Carregar as reservas do usuário logado
        loadUserReservations()


        return binding.root
    }



    private fun setupRecyclerView() {
        binding.recyclerViewReservations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reservationAdapter
        }
    }

    private fun loadUserReservations() {
        lifecycleScope.launch {
            val userId = getCurrentUserId()
            val reservations = getUserReservations(userId)
            reservationAdapter.submitList(reservations)
        }
    }

    private suspend fun getUserReservations(userId: Long): List<Reserva> {
        return withContext(Dispatchers.IO) {
            val reservaDao = CarDatabase.getInstance(requireContext()).ReservaDao()
            reservaDao.getReservasByUser(userId) // Obtém as reservas associadas ao ID do usuário
        }
    }

    private fun getCurrentUserId(): Long {
        // Aqui você pode substituir pela lógica para pegar o ID do usuário logado
        return 1L // Exemplo, substitua por sua lógica de login
    }

    private fun handleReviewClick(reservation: Reserva) {
        // Exemplo de lógica ao clicar no botão de review
        Toast.makeText(requireContext(), "Fazer review para reserva: ${reservation.id}", Toast.LENGTH_SHORT).show()

        // Aqui você pode abrir um novo fragmento, diálogo ou tela para o review
        // Por exemplo:
        // val action = UserReservationsFragmentDirections.actionToReviewFragment(reservation.idReserva)
        // findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}