package com.conoland.pokedex

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conoland.pokedex.models.Pokemon

class Detalles_PojkemonAdapter(
    private val context: Context,
    private val parent: ListaPokemonAdapter
) :
    RecyclerView.Adapter<Detalles_PojkemonAdapter.ViewHolder>() {
    private val listaDetalles = ArrayList<Pokemon>()

    fun adicionarDetallesPokemon(list: ArrayList<Pokemon>) {
        listaDetalles.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_etails_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = listaDetalles[position]
        holder.name.text = p.name
        holder.weight.text = "PESO: " + p.weight + "Kg"
        Glide.with(holder.name.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p.number + ".png")
            .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img)
    }

    override fun getItemCount(): Int {
        return listaDetalles.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView =
            itemView.findViewById(R.id.fotoD)
        var name: TextView = itemView.findViewById(R.id.nombreD)
        var weight: TextView = itemView.findViewById(R.id.pesoD)
        var volver: TextView = itemView.findViewById(R.id.volver)

        init {
            volver.setOnClickListener { view: View? ->
                val activity = (context as Activity)
                val recyclerView =
                    activity.findViewById<RecyclerView>(R.id.recyclerView)
                val layoutManager = GridLayoutManager(context, 2)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = parent
            }
        }
    }
}
