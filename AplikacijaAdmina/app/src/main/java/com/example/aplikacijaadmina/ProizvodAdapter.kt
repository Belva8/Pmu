package com.example.aplikacijaadmina

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProizvodAdapter(
    var ListProizvod: List<Trgovina>,
    private val itemClickListener: OnItemClickListener
)
    : RecyclerView.Adapter<ProizvodAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val IVProizvoda: ImageView = itemView.findViewById(R.id.IVProizvoda)
        val tvProizvod: TextView = itemView.findViewById(R.id.TVNazivProizvoda)
        val tvCijena: TextView = itemView.findViewById(R.id.CijenaProizvoda)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.proizvod, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val proizvod = ListProizvod[position]

        holder.tvProizvod.text = " ${proizvod.naziv}"
        holder.tvCijena.text = " ${proizvod.cijena} €"

        Glide.with(holder.IVProizvoda.context)
            .load(proizvod.imageUrl)
            .into(holder.IVProizvoda)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(proizvod)
        }
    }

    override fun getItemCount(): Int {
        return ListProizvod.size
    }

    interface OnItemClickListener {
        fun onItemClick(Proizvod : Trgovina)
    }
}
