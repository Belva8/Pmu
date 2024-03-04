package com.example.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransakcijaAdapter(private val transakcijaList: List<zahtjev>) :
    RecyclerView.Adapter<TransakcijaAdapter.TransakcijaViewHolder>() {

    class TransakcijaViewHolder(itemView: View) : Transakcija.ViewHolder(itemView) {
        val tvuser: TextView = itemView.findViewById(R.id.tvUser)
        val tvBrojRacuna: TextView = itemView.findViewById(R.id.tvBrojRacuna)
        val tvStanjeRacuna: TextView = itemView.findViewById(R.id.tvStanjeRacuna)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransakcijaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transakcija, parent, false)
        return TransakcijaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrnsakcijaViewHolder, position: Int) {
        val transakcija = transakcijaList[position]

        holder.tvUser.text = transakcija.e-mail
        holder.tvBrojRacuna.text = transakcija.brojRacuna
        holder.tvStanjeRacuna.text = transakcija.stanjeRacuna
    }

    override fun getItemCount(): Int {
        return transakcijaList.size
    }
}
