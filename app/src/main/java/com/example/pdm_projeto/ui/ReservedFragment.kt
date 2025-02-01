package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Reserva
import com.example.pdm_projeto.databinding.FragmentReservadoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * **ReservedFragment** exibe os detalhes da reserva do veículo.
 * Ele obtém os dados de reserva do banco de dados e atualiza a interface do usuário.
 * Também fornece uma funcionalidade de navegação para retornar ao fragmento anterior.
 */
class ReservedFragment : Fragment() {

    private var _binding: FragmentReservadoBinding? = null // Referência segura para o binding
    private val binding get() = _binding!! // Acesso seguro ao binding

    private val args: ReservedFragmentArgs by navArgs() // Obtém os argumentos passados ao fragmento

    /**
     * Chamado quando a view do fragmento é criada.
     * Aqui, a interface do usuário (UI) é inflada e os dados de reserva são carregados.
     *
     * @param inflater O objeto utilizado para inflar o layout.
     * @param container O container onde a view será colocada.
     * @param savedInstanceState O estado salvo da instância anterior, se disponível.
     * @return A view inflada para o fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout utilizando o view binding
        _binding = FragmentReservadoBinding.inflate(inflater, container, false)

        // Carrega os detalhes da reserva
        loadReservationDetails()

        // Configura o botão de voltar
        setupBackButton()

        return binding.root
    }

    /**
     * Função que obtém os detalhes da reserva no banco de dados.
     * Ela consulta o banco de dados para obter a reserva correspondente ao veículo e ao usuário.
     *
     * @param vehicleId O ID do veículo.
     * @param userId O ID do usuário.
     * @return A reserva correspondente ou null se não encontrar.
     */
    private suspend fun getReservationDetails(vehicleId: Long, userId: Long): Reserva? {
        return withContext(Dispatchers.IO) {
            val reservaDao = CarDatabase.getInstance(requireContext()).ReservaDao()
            reservaDao.getReservasByUser(userId).find { it.idCar == vehicleId }
        }
    }

    /**
     * Função que carrega os detalhes da reserva e atualiza a interface do usuário.
     * Exibe informações sobre o carro reservado e os horários de início e fim da reserva.
     */
    private fun loadReservationDetails() {
        lifecycleScope.launch {
            val reservation = getReservationDetails(args.vehicleId, args.userId)
            if (reservation != null) {
                // Atualiza a UI com os detalhes da reserva
                val carDetails = "Car Reserved: ${args.vehicleName}\nDetails: ${args.vehicleDetails}"
                binding.tvReservationDetails.text = carDetails

                val reservationTime = """
                    Start Time: ${reservation.dataInicio}
                    End Time: ${reservation.dataFim}
                """.trimIndent()
                binding.tvReservationTime.text = reservationTime
            } else {
                // Caso não encontre a reserva, exibe uma mensagem de erro
                binding.tvReservationDetails.text = "No reservation found."
                binding.tvReservationTime.text = ""
            }
        }
    }

    /**
     * Função que configura o botão de voltar para retornar ao fragmento anterior.
     */
    private fun setupBackButton() {
        binding.floatingActionButton3.setOnClickListener {
            findNavController().navigate(
                findNavController().popBackStack() // Navega para o fragmento anterior
            )
        }
    }

    /**
     * Chamado quando a view do fragmento é destruída. Limpa a referência ao binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
