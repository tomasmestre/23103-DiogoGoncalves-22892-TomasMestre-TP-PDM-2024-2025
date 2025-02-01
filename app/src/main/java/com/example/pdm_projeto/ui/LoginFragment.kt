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
import com.example.pdm_projeto.databinding.FragmentLoginBinding
import com.example.pdm_projeto.ui.modelview.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento responsável pela tela de login do aplicativo.
 * Permite que os usuários façam login com credenciais válidas ou acessem a tela de registro.
 */
class LoginFragment : Fragment() {

    private val viewModel by viewModels<loginViewModel>() // Instância do ViewModel para autenticação
    private lateinit var binding: FragmentLoginBinding

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
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Configuração do botão para navegar até a tela de registro
        binding.LoginRegisterButton.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }

        return binding.root
    }

    /**
     * Chamado após a visualização ter sido criada.
     * Aqui, é configurado o botão de login.
     *
     * @param view A view raiz do fragmento.
     * @param savedInstanceState O estado salvo da instância anterior do fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar o botão de login
        binding.buttonLogin.setOnClickListener {
            logIn()
        }
    }

    /**
     * Realiza o processo de login do usuário.
     * Verifica se os campos de email e senha estão preenchidos e, em seguida,
     * consulta o banco de dados para validar as credenciais.
     */
    private fun logIn() {
        val emailLogIn = binding.LogInName.text?.toString()?.trim()
        val passwordLogIn = binding.LogInPassword.text?.toString()?.trim()

        // Verificar se os campos estão preenchidos
        if (emailLogIn.isNullOrEmpty() || passwordLogIn.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val user = viewModel.getUserByEmail(emailLogIn)
                withContext(Dispatchers.Main) {
                    when {
                        emailLogIn == "admin" && passwordLogIn == "admin" -> {
                            // Login de Admin
                            Toast.makeText(requireContext(), "Bem-vindo, Admin!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToAdminCarListFragment()
                            )
                        }
                        user != null && user.password == passwordLogIn -> {
                            // Login de usuário comum
                            Toast.makeText(requireContext(), "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment(userId = user.id)
                            )
                        }
                        else -> {
                            // Credenciais inválidas
                            Toast.makeText(requireContext(), "Credenciais inválidas.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Erro ao realizar login.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
