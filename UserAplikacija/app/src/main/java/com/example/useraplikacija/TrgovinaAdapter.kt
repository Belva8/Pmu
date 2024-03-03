package com.example.useraplikacija

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class TrgovinaAdapter(var ProizvodItemList: List<Trgovina>) :
    RecyclerView.Adapter<TrgovinaAdapter.TrgovinaViewHolder>() {

    class TrgovinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProizvoda: ImageView = itemView.findViewById(R.id.ivProizvoda)
        val tvNazivProizvoda: TextView = itemView.findViewById(R.id.tvNazivProizvoda)
        val tvCijenaProizvoda: TextView = itemView.findViewById(R.id.CijenaProizvoda)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrgovinaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return TrgovinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrgovinaViewHolder, position: Int) {
        val trgovina = ProizvodItemList[position]

        holder.tvNazivProizvoda.text = trgovina.naziv
        holder.tvCijenaProizvoda.text = trgovina.cijena

        // Prikazivanje slike pomoću Glide biblioteke
        Glide.with(holder.ivProizvoda.context)
            .load(trgovina.imageUrl) // Koristite imageUrl koji ste dobili iz Realtime Database-a
            .into(holder.ivProizvoda)

        holder.itemView.setOnClickListener {
            // Ovdje možete dodati akciju za poziv kada se klikne na stavku
            // Primjer: itemClickListener.onItemClick(restoran)
        }
    }

    override fun getItemCount(): Int {
        return ProizvodItemList.size
    }

    // Možete dodati setOnItemClickListener metodu i odgovarajući interfejs kao u JeloAdapteru ako želite.
    // Ovisi o vašim potrebama i kako želite obrađivati klikove na stavke RecyclerView-a.
}
