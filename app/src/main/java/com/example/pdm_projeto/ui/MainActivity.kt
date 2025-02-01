package com.example.pdm_projeto.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pdm_projeto.R

/**
 * **MainActivity** é a atividade principal do aplicativo.
 * Ela configura a interface do usuário e gerencia o comportamento global da aplicação.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Chamado quando a atividade é criada.
     * Configura a interface do usuário, ativando o **edge-to-edge display** e aplicando
     * ajustes de preenchimento para considerar as barras do sistema.
     *
     * @param savedInstanceState Estado salvo da instância anterior da atividade, se disponível.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ativa o modo de exibição em tela cheia
        setContentView(R.layout.activity_main)

        // Ajusta os paddings da view principal para evitar sobreposição com barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
