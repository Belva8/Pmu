package com.example.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BankaAdapter(var racunItemList: List<Banka>) :
    RecyclerView.Adapter<BankaAdapter.BankaViewHolder>() {

    class BankaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivRacun: ImageView = itemView.findViewById(R.id.ivRacun)
        val tvBrojRacuna: TextView = itemView.findViewById(R.id.BrojRacuna)
        val tvStanjeRacuna: TextView = itemView.findViewById(R.id.tvStanjeRacuna)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return BankaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BankaViewHolder, position: Int) {
        val banka = racunItemList[position]

        holder.tvBrojRacuna.text = banka.broj
        holder.tvStanjeRacuna.text = banka.stanje

        // Prikazivanje slike pomoću Glide biblioteke
        Glide.with(holder.ivRacun.context)
            .load(banka.imageUrl) // Koristite imageUrl koji ste dobili iz Realtime Database-a
            .into(holder.ivRacun)

        holder.itemView.setOnClickListener {
            // Ovdje možete dodati akciju za poziv kada se klikne na stavku
            // Primjer: itemClickListener.onItemClick(banka)
        }
    }

    override fun getItemCount(): Int {
        return racunItemList.size
    }

    // Možete dodati setOnItemClickListener metodu i odgovarajući interfejs kao u RacunAdapteru ako želite.
    // Ovisi o vašim potrebama i kako želite obrađivati klikove na stavke RecyclerView-a.
}
