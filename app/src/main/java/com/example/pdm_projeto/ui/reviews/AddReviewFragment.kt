package com.example.pdm_projeto.ui.reviews

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Review
import com.example.pdm_projeto.databinding.FragmentAddReviewBinding
import com.example.pdm_projeto.ui.ReservedFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AddReviewFragment : Fragment() {

    private var _binding: FragmentAddReviewBinding? = null
    private val binding get() = _binding!!

    private val REQUEST_IMAGE_CAPTURE = 1
    private var capturedImage: Bitmap? = null

    private val args: ReservedFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddReviewBinding.inflate(inflater, container, false)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        // Botão para tirar foto
        binding.btnTakePhoto.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                Toast.makeText(requireContext(), "Não é possível acessar a câmera", Toast.LENGTH_SHORT).show()
            }
        }

        // Botão para salvar a review
        binding.btnSaveReview.setOnClickListener {
            val reviewText = binding.etReviewText.text.toString()
            val rating = binding.ratingBar.rating

            if (reviewText.isBlank() || rating == 0f) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                // Salvar a review no banco de dados
                val imagePath = saveImageToStorage(capturedImage)
                saveReviewToDatabase(reviewText, rating, imagePath)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            capturedImage = imageBitmap
            binding.ivReviewImage.setImageBitmap(capturedImage) // Exibir a imagem capturada
        }
    }

    private fun saveImageToStorage(bitmap: Bitmap?): String? {
        if (bitmap == null) return null

        val filename = "review_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, filename)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }

        return file.absolutePath
    }

    private fun saveReviewToDatabase(reviewText: String, rating: Float, imagePath: String?) {
        // Obter o ID da reserva (supondo que seja passado via argumentos)
        val reservationId = arguments?.getLong("reservationId")
        if (reservationId == null) {
            Toast.makeText(requireContext(), "Erro: ID da reserva não encontrado!", Toast.LENGTH_SHORT).show()
            return
        }

        val review = Review(
            reservationId = reservationId,
            reviewText = reviewText,
            rating = rating,
            imagePath = imagePath
        )

        // Salvar a review no banco usando coroutines
        lifecycleScope.launch(Dispatchers.IO) {
            val db = CarDatabase.getInstance(requireContext())
            db.ReviewDao().insertReview(review)

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Review salva com sucesso!", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed() // Voltar para a tela anterior
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
