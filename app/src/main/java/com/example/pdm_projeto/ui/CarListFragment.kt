package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.CarDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment responsible for displaying a list of available cars.
 * Users can select a car to view its details or navigate to the home screen.
 */
class CarListFragment : Fragment() {

    /**
     * Called when the fragment is first created.
     * Currently, this method does not perform any specific initialization.
     *
     * @param savedInstanceState A Bundle containing the saved state of the fragment, if available.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Creates and returns the view associated with this fragment.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's view.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the saved state of the fragment, if available.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_list, container, false)

        // RecyclerView setup
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCars)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Access the database and fetch cars
        val carDao = CarDatabase.getInstance(requireContext()).CarDao()

        lifecycleScope.launch {
            val cars = withContext(Dispatchers.IO) { carDao.getAllCars() } // Fetch cars from DB
            recyclerView.adapter = VehicleAdapter(cars.toMutableList()) { selectedVehicle ->
                // Pass the selected vehicle details as parameters to the CarDetailsFragment
                val action = CarListFragmentDirections.actionCarListFragmentToCarDetailsFragment(
                    selectedVehicle.id.toLong(), // vehicleId
                    1L, // userId (TODO: Replace with actual user ID retrieval logic)
                    selectedVehicle.name, // vehicleName
                    selectedVehicle.details // vehicleDetails
                )
                findNavController().navigate(action)
            }
        }

        return view
    }
}
