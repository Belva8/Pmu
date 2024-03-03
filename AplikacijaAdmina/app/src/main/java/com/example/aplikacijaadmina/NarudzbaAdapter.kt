package com.example.aplikacijaadmina

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NarudzbaAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NarudzbaAdapter.NarudzbaViewHolder>() {

    var narudzbaList: List<Zahtjev> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class NarudzbaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TVKupac: TextView = itemView.findViewById(R.id.TVKupac)
        val TVNazivProizvoda: TextView = itemView.findViewById(R.id.tvNazivProizvoda)
        val TVCijenaProizvoda: TextView = itemView.findViewById(R.id.tvCijenaProizvoda)
    }

    interface OnItemClickListener {
        fun onItemClick(zahtjev: Zahtjev)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NarudzbaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.narudzba, parent, false)
        return NarudzbaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NarudzbaViewHolder, position: Int) {
        val narudzba = narudzbaList[position]

        holder.TVKupac.text = narudzba.email
        holder.TVNazivProizvoda.text = narudzba.nazivProizvoda
        holder.TVCijenaProizvoda.text = narudzba.cijenaProizvoda

        // Set visibility based on the 'odobren' property
        holder.itemView.visibility = if (narudzba.odobren == 1) View.GONE else View.VISIBLE

        holder.itemView.setOnClickListener {
            listener.onItemClick(narudzba)
        }
    }

    override fun getItemCount(): Int {
        return narudzbaList.size
    }
}
