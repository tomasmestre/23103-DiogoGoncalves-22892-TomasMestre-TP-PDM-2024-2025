package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment responsible for adding a new car to the database.
 */
class AdminAddCar : Fragment() {

    /**
     * Creates and returns the view associated with this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If not null, this is the parent view to which the fragment's UI should be attached.
     * @param savedInstanceState If not null, this fragment is being reconstructed from a previous saved state.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_add_car, container, false)

        // Getting references to input fields
        val etBrand = view.findViewById<EditText>(R.id.etMarca)
        val etDescription = view.findViewById<EditText>(R.id.etDescricao)
        val etPrice = view.findViewById<EditText>(R.id.etPreco)
        val switchState = view.findViewById<Switch>(R.id.switchEstado)

        val btnAddCar = view.findViewById<View>(R.id.btnAddCar)

        // Button click listener
        btnAddCar.setOnClickListener {
            val brand = etBrand.text.toString()
            val description = etDescription.text.toString()
            val price = etPrice.text.toString().toFloatOrNull()
            val state = switchState.isChecked


            // Validate input fields
            if (brand.isBlank() || description.isBlank() || price == null) {
                Toast.makeText(requireContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new Vehicle object
            val newCar = Vehicle(
                id = 0, // ID will be auto-generated if set to 0
                name = brand,
                details = description,
                price = price,
                state = state
            )

            // Insert into the database
            lifecycleScope.launch {
                try {
                    val carDao = CarDatabase.getInstance(requireContext()).CarDao()
                    withContext(Dispatchers.IO) {
                        carDao.insertAll(listOf(newCar)) // Insert the car into the database
                    }
                    Toast.makeText(requireContext(), "Car added successfully!", Toast.LENGTH_SHORT).show()

                    // Navigate back to the previous screen
                    findNavController().navigateUp()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error adding car: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
