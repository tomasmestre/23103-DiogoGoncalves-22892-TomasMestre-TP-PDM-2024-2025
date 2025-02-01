package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Role
import com.example.pdm_projeto.data.Utilizador
import com.example.pdm_projeto.databinding.FragmentRegisterBinding
import com.example.pdm_projeto.ui.modelview.loginViewModel
import kotlinx.coroutines.launch

/**
 * **RegisterFragment** gerencia o processo de registro de um novo usuário.
 * Ele coleta as informações do usuário, valida o formulário e registra o usuário no banco de dados.
 */
class RegisterFragment : Fragment() {

    private val viewModel by viewModels<loginViewModel>() // Instância do ViewModel responsável por manipulação de dados de login
    private lateinit var binding: FragmentRegisterBinding // Referência para os elementos de UI

    /**
     * Chamado quando a view do fragmento é criada.
     * Aqui, a interface do usuário (UI) do fragmento é inflada.
     *
     * @param inflater O objeto utilizado para inflar o layout.
     * @param container O container onde a view será colocada.
     * @param savedInstanceState O estado salvo da instância anterior, se disponível.
     * @return A view inflada para o fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false) // Infla o layout do fragmento
        return binding.root
    }

    /**
     * Chamado após a criação da view. Aqui, configuramos os listeners dos botões.
     *
     * @param view A view da interface do fragmento.
     * @param savedInstanceState O estado salvo da instância anterior, se disponível.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonRegisto.setOnClickListener {
            createRegisto() // Inicia o processo de criação do registro de usuário
        }

        binding.ReturnToLogin.setOnClickListener {
            findNavController().popBackStack() // Retorna à tela de login
        }
    }

    /**
     * Função que cria o registro do usuário após validação.
     *
     * Ela coleta as informações inseridas no formulário de registro, valida as entradas e registra o novo usuário.
     */
    private fun createRegisto() {
        val nome = binding.nomeRegisto.text.toString()
        val email = binding.emailRegisto.text.toString()
        val password = binding.passwordRegisto.text.toString()

        // Verifica se os campos estão vazios
        if (nome.isBlank() || email.isBlank() || password.isBlank()) {
            return
        }

        // Cria um novo objeto Utilizador
        val newUser = Utilizador(
            nome = nome,
            email = email,
            password = password,
            role = Role.USER // O novo usuário tem o papel de "usuário comum"
        )

        lifecycleScope.launch {
            // Verifica se já existe um usuário com o mesmo e-mail
            val existingUser = viewModel.getUserByEmail(email)
            if (existingUser != null) {
                Toast.makeText(requireContext(), "Email já registado.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Cria o usuário no banco de dados
            val dao = viewModel.createUser(newUser)

            // Verifica se a inserção foi bem-sucedida
            val insertedUser = viewModel.getUserByEmail(email)
            if (dao != null) {
                println("Utilizador registado com sucesso: $insertedUser")
            } else {
                println("Erro ao registar o utilizador.")
            }

            // Retorna à tela anterior
            findNavController().popBackStack()
        }
    }
}
