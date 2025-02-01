package com.example.pdm_projeto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.CarDatabase
import com.example.pdm_projeto.data.Estado
import com.example.pdm_projeto.data.Reserva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment responsible for displaying car details and handling car reservations.
 */
class CarDetailsFragment : Fragment() {

    private val args: CarDetailsFragmentArgs by navArgs()

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
        val view = inflater.inflate(R.layout.fragment_car_details, container, false)

        // Get references to views
        val nameTextView: TextView = view.findViewById(R.id.tvVehicleName)
        val detailsTextView: TextView = view.findViewById(R.id.tvVehicleDetails)
        val startTimeEditText: EditText = view.findViewById(R.id.etStartTime)
        val endTimeEditText: EditText = view.findViewById(R.id.etEndTime)
        val confirmButton: Button = view.findViewById(R.id.btnReserveCar)

        // Set vehicle details
        nameTextView.text = args.vehicleName
        detailsTextView.text = args.vehicleDetails

        // Handle reservation button click
        confirmButton.setOnClickListener {
            val startTime = startTimeEditText.text.toString().toFloatOrNull()
            val endTime = endTimeEditText.text.toString().toFloatOrNull()

            if (startTime == null || endTime == null || startTime >= endTime) {
                Toast.makeText(requireContext(), "Invalid reservation times!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Perform the reservation in a coroutine
            lifecycleScope.launch {
                val success = withContext(Dispatchers.IO) {
                    checkAndReserveCar(startTime, endTime)
                }

                if (success) {
                    Toast.makeText(requireContext(), "Reservation successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        CarDetailsFragmentDirections.actionCarDetailsFragmentToReservedFragment(
                            vehicleId = args.vehicleId,
                            userId = args.userId,
                            vehicleName = args.vehicleName,
                            vehicleDetails = args.vehicleDetails
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Failed to reserve the car.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    /**
     * Checks if a car is available and reserves it if possible.
     *
     * @param startTime The start time of the reservation.
     * @param endTime The end time of the reservation.
     * @return `true` if the reservation was successful, `false` otherwise.
     */
    private suspend fun checkAndReserveCar(startTime: Float, endTime: Float): Boolean {
        return try {
            val db = CarDatabase.getInstance(requireContext())
            val carDao = db.CarDao()
            val reservaDao = db.ReservaDao()

            val vehicle = carDao.getCarById(args.vehicleId)

            // Check if the car is already reserved within the given time range
            val existingReservations = reservaDao.isCarReservedInRange(args.vehicleId, startTime, endTime)
            if (existingReservations.isNotEmpty() || !vehicle.state) {
                return false
            }

            // Create and insert the new reservation
            val newReservation = Reserva(
                dataInicio = startTime,
                dataFim = endTime,
                estado = Estado.UTILIZACAO,
                idCar = vehicle.id.toLong(),
                idUtilizador = args.userId
            )
            reservaDao.insert(newReservation)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
