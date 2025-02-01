package com.example.pdm_projeto.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.databinding.FragmentViewReviewsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewReviewsFragment : Fragment() {

    private var _binding: FragmentViewReviewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var reviewsAdapter: ReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewReviewsBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadReviews()

        return binding.root
    }

    private fun setupRecyclerView() {
        reviewsAdapter = ReviewsAdapter()
        binding.recyclerViewReviews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewsAdapter
        }
    }

    private fun loadReviews() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = CarDatabase.getInstance(requireContext())
            val reviews = db.ReviewDao().getAllReviews() // Assume que você já tem este método no DAO

            withContext(Dispatchers.Main) {
                reviewsAdapter.submitList(reviews)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}