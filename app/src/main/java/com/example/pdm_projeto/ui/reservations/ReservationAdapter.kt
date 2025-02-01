package com.example.pdm_projeto.ui.reservations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.Reserva
import com.example.pdm_projeto.ui.ReservedFragment

class ReservationAdapter(private val onReviewClick: (Reserva) -> Unit) :
    RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    private val reservations = mutableListOf<Reserva>()

    fun submitList(newReservations: List<Reserva>) {
        reservations.clear()
        reservations.addAll(newReservations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return ReservationViewHolder(view, onReviewClick) // Passando a função para o ViewHolder
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.bind(reservation)
    }

    override fun getItemCount(): Int = reservations.size

    class ReservationViewHolder(
        itemView: View,
        private val onReviewClick: (Reserva) -> Unit // Recebe a função diretamente no construtor
    ) : RecyclerView.ViewHolder(itemView) {
        private val carDetailsTextView: TextView = itemView.findViewById(R.id.tvCarDetails)
        private val reservationPeriodTextView: TextView = itemView.findViewById(R.id.tvReservationPeriod)
        private val reviewButton: Button = itemView.findViewById(R.id.btnReview)

        fun bind(reservation: Reserva) {
            carDetailsTextView.text = "Carro ID: ${reservation.idCar}"
            reservationPeriodTextView.text = "Período: ${reservation.dataInicio} - ${reservation.dataFim}"

            // Configurar o clique no botão de review
         /*   reviewButton.setOnClickListener {
                onReviewClick(reservation)
            }*/

            reviewButton.setOnClickListener {
                findNavController(itemView).navigate(
                    UserReservationsFragmentDirections.actionUserReservationsFragmentToAddReviewFragment(reservation.id)
                )
            }
        }
    }
}
