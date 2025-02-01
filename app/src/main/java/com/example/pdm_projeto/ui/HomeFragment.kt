package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Reserva
import com.example.pdm_projeto.data.Vehicle
import com.example.pdm_projeto.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento responsável pela tela inicial do aplicativo.
 * Permite ao usuário navegar para a lista de carros, visualizar reservas,
 * acessar detalhes da reserva mais recente e consultar avaliações.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val args: HomeFragmentArgs by navArgs() // Obtém os argumentos passados para o fragmento
    private var userId: Long = -1L // ID do usuário, inicializado como inválido até ser definido corretamente

    /**
     * Cria e retorna a visualização associada a este fragmento.
     *
     * @param inflater O LayoutInflater usado para inflar a view do fragmento.
     * @param container O contêiner pai onde a UI do fragmento será anexada.
     * @param savedInstanceState Um Bundle contendo o estado salvo do fragmento, se disponível.
     * @return A raiz da visualização do fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        userId = args.userId // Obtém o userId dos argumentos

        // Configurar os listeners para os botões
        binding.goToListButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCarListFragment())
        }
        binding.btnViewReservations.setOnClickListener {
            // Navega para a tela de reservas do usuário
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUserReservationsFragment())
        }

        binding.goToReservedButton.setOnClickListener {
            lifecycleScope.launch {
                val reserva = getLatestReservation(userId)
                if (reserva != null) {
                    val vehicle = getVehicleDetails(reserva.idCar)
                    val action = HomeFragmentDirections.actionHomeFragmentToReservedFragment(
                        vehicleId = reserva.idCar,
                        userId = userId,
                        vehicleName = vehicle.name,
                        vehicleDetails = vehicle.details
                    )
                    findNavController().navigate(action)
                }
            }
        }

        binding.Review.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewReviewsFragment())
        }

        return binding.root
    }

    /**
     * Obtém a última reserva do usuário a partir do banco de dados.
     *
     * @param userId O ID do usuário.
     * @return A reserva mais recente ou `null` caso o usuário não tenha reservas.
     */
    private suspend fun getLatestReservation(userId: Long): Reserva? {
        return withContext(Dispatchers.IO) {
            val reservaDao = CarDatabase.getInstance(requireContext()).ReservaDao()
            reservaDao.getReservasByUser(userId).lastOrNull()
        }
    }

    /**
     * Obtém os detalhes de um veículo pelo seu ID.
     *
     * @param vehicleId O ID do veículo.
     * @return Um objeto [Vehicle] contendo os detalhes do veículo.
     */
    private suspend fun getVehicleDetails(vehicleId: Long): Vehicle {
        return withContext(Dispatchers.IO) {
            val carDao = CarDatabase.getInstance(requireContext()).CarDao()
            carDao.getCarById(vehicleId)
        }
    }

    /**
     * Limpa a referência ao binding quando a visualização do fragmento é destruída.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
