package com.baag.treksahayatri.ui.hotel


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.Reservation


class ReservationAdapter(
    private val list: List<Reservation>,
    private val onAccept: (Reservation) -> Unit,
    private val onReject: (Reservation) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ResViewHolder>() {

    inner class ResViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvTravellerName)
        val date: TextView = view.findViewById(R.id.tvDate)
        val time: TextView = view.findViewById(R.id.tvTime)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val room: TextView = view.findViewById(R.id.tvRoomCategory)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnReject: Button = view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation, parent, false)
        return ResViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResViewHolder, position: Int) {
        val res = list[position]

        holder.name.text = "Traveller: ${res.travellerName}"
        holder.date.text = "Date: ${res.date}"
        holder.time.text = "Time: ${res.time}"
        holder.status.text = "Status: ${res.status}"
        holder.room.text = "Room: ${res.roomCategory}"

        val isMock = res.id.startsWith("mock")
        val isPending = res.status == "pending"

        // Enable buttons only for real & pending reservations
        holder.btnAccept.isEnabled = !isMock && isPending
        holder.btnReject.isEnabled = !isMock && isPending

        if (!isMock) {
            holder.btnAccept.setOnClickListener { onAccept(res) }
            holder.btnReject.setOnClickListener { onReject(res) }
        }
    }

    override fun getItemCount(): Int = list.size
}
