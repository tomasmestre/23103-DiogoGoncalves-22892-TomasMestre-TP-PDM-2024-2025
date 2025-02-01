package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment responsible for displaying a list of cars for the admin.
 * Allows adding and deleting cars.
 */
class AdminCarListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VehicleAdapter
    private var cars: MutableList<Vehicle> = mutableListOf() // Initialize as empty

    /**
     * Creates and returns the view associated with this fragment.
     *
     * @param inflater The LayoutInflater object used to inflate the fragment's view.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the saved state of the fragment, if available.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_car_list, container, false)

        // Setup RecyclerView
        setupRecyclerView(view)

        // Fetch data from the database
        fetchData()

        // Add Car button
        val btnAddCar = view.findViewById<Button>(R.id.btnAddCar)
        btnAddCar.setOnClickListener {
            findNavController().navigate(R.id.action_adminCarListFragment_to_addCarFragment)
        }

        // Delete Car button
        val btnDeleteCar = view.findViewById<Button>(R.id.btnDeleteCar)
        btnDeleteCar.setOnClickListener {
            if (cars.isNotEmpty()) {
                cars.removeAt(cars.size - 1)
                adapter.updateData(cars)
                Toast.makeText(requireContext(), "Car deleted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No cars to delete!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and initializes the adapter.
     *
     * @param view The root view of the fragment.
     */
    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewAdminCars)
        adapter = VehicleAdapter(cars) { selectedVehicle ->
            Toast.makeText(requireContext(), "Selected: ${selectedVehicle.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    /**
     * Fetches the list of cars from the database and updates the RecyclerView.
     * Uses coroutines to perform database operations asynchronously.
     */
    private fun fetchData() {
        lifecycleScope.launch {
            try {
                val carDao = CarDatabase.getInstance(requireContext()).CarDao()
                val vehicles = withContext(Dispatchers.IO) {
                    carDao.getAllCars()
                }
                cars.clear()
                cars.addAll(vehicles)
                adapter.updateData(cars)
                // Log the fetched cars
                vehicles.forEach {
                    println("Car: ${it.name}")
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error loading cars: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
