package com.example.pdm_projeto.ui.reviews

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_projeto.data.Review
import com.example.pdm_projeto.databinding.ItemReviewBinding

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    private var reviews: List<Review> = listOf()

    fun submitList(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.tvReviewText.text = review.reviewText
            binding.ratingBar.rating = review.rating

            // Exibe a imagem da review se disponível
            if (review.imagePath != null) {
                val bitmap = BitmapFactory.decodeFile(review.imagePath)
                if (bitmap != null) {
                    binding.ivReviewImage.setImageBitmap(bitmap) // Define a imagem no ImageView
                    binding.ivReviewImage.visibility = View.VISIBLE // Certifica-se de que a imagem é exibida
                } else {
                    binding.ivReviewImage.visibility = View.GONE // Caso o arquivo esteja corrompido ou inválido
                }
            } else {
                binding.ivReviewImage.visibility = View.GONE // Esconde o ImageView se não houver imagem
            }
        }
    }
}
