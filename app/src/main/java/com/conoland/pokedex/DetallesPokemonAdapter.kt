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

class DetallesPokemonAdapter(
    private val context: Context,
    private val parent: ListaPokemonAdapter
) : RecyclerView.Adapter<DetallesPokemonAdapter.ViewHolder>() {

    private val listaDetalles = ArrayList<Pokemon>()

    fun adicionarDetallesPokemon(list: ArrayList<Pokemon>) {
        val prevSize = listaDetalles.size
        listaDetalles.addAll(list)
        notifyItemRangeInserted(prevSize, list.size) // Mejora el rendimiento
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_etails_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = listaDetalles[position]
        holder.name.text = p.name
        holder.weight.text = "PESO: ${p.weight}Kg"

        Glide.with(holder.img.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/${p.number}.gif")
            .thumbnail(0.1f) // Precarga para una mejor experiencia
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.img)
    }

    override fun getItemCount(): Int = listaDetalles.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.fotoD)
        var name: TextView = itemView.findViewById(R.id.nombreD)
        var weight: TextView = itemView.findViewById(R.id.pesoD)
        var volver: TextView = itemView.findViewById(R.id.volver)

        init {
            volver.setOnClickListener {
                val activity = context as Activity
                val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
                val prefs = context.getSharedPreferences("PokedexPrefs", Context.MODE_PRIVATE)
                val posicionGuardada = prefs.getInt("posicion_lista", 0)

                recyclerView.adapter = parent
                recyclerView.layoutManager = GridLayoutManager(context, 2)
                recyclerView.scrollToPosition(posicionGuardada)
            }
        }
    }
}
