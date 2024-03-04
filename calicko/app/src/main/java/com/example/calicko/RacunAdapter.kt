package com.example.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RacunAdapter(
    var racunItemList: List<Banka>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RacunAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivRacun: ImageView = itemView.findViewById(R.id.ivRacun)
        val tvRacun: TextView = itemView.findViewById(R.id.tvRacun)
        val tvStanjeRacuna: TextView = itemView.findViewById(R.id.tvStanjeRacuna)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val racunItem = racunItemList[position]

        holder.tvRacun.text = RacunItem.broj
        holder.tvStanjeRacuna.text = racunItem.stanje

        Glide.with(holder.ivRacun.context)
            .load(racunItem.imageUrl)
            .into(holder.ivRacun)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(racunItem)
        }
    }

    override fun getItemCount(): Int {
        return racunItemList.size
    }

    interface OnItemClickListener {
        fun onItemClick(racun: Banka)
    }
}
