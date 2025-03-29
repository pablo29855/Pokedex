package com.conoland.pokedex
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conoland.pokedex.models.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.stream.Collectors

class ListaPokemonAdapter(private val context: Context) :
    RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder>() {

    private val dataset = ArrayList<Pokemon>()
    private val listaOriginal = ArrayList<Pokemon>()

    init {
        listaOriginal.addAll(dataset)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = dataset[position]
        holder.nombre.text = p.name
        holder.order = p.number

        Glide.with(holder.foto.context) // 💡 Debe ser el contexto de la vista donde se carga la imagen
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${p.number}.png")
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.foto)
    }

    fun filtrado(buscar: String) {
        dataset.clear()

        if (buscar.isEmpty()) {
            dataset.addAll(listaOriginal)
        } else {
            val filtro = buscar.lowercase(Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dataset.addAll(listaOriginal.stream()
                    .filter { it.name!!.lowercase(Locale.getDefault()).contains(filtro) }
                    .collect(Collectors.toList()))
            } else {
                for (pokemon in listaOriginal) {
                    if (pokemon.name!!.lowercase(Locale.getDefault()).contains(filtro)) {
                        dataset.add(pokemon)
                    }
                }
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataset.size

    fun adicionarListaPokemon(listaPokemon: List<Pokemon>) {
        dataset.addAll(listaPokemon)
        listaOriginal.addAll(listaPokemon)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto: ImageView = itemView.findViewById(R.id.foto)
        val nombre: TextView = itemView.findViewById(R.id.nombre)
        var order: Int = 0

        init {
            foto.setOnClickListener {
                val parentActivity = context as Activity
                val adapter = Detalles_PojkemonAdapter(context, this@ListaPokemonAdapter)
                datosPokemon(adapter, order)

                parentActivity.findViewById<View>(R.id.buscar).visibility = View.GONE
                val recyclerView = parentActivity.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = adapter
            }
        }
    }

    private fun datosPokemon(adapter: Detalles_PojkemonAdapter, pokemonNum: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PokeapiService::class.java)
        val pokemonRespuestaCall = service.datos(pokemonNum)

        pokemonRespuestaCall.enqueue(object : Callback<Pokemon?> {
            override fun onResponse(call: Call<Pokemon?>, response: Response<Pokemon?>) {
                if (response.isSuccessful) {
                    response.body()?.let { pokemon ->
                        adapter.adicionarDetallesPokemon(ArrayList(listOf(pokemon)))
                    }
                } else {
                    Log.e(javaClass.simpleName, "Error en la respuesta: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Pokemon?>, t: Throwable) {
                Log.e(javaClass.simpleName, "Error en la petición: ${t.message}")
            }
        })
    }
}

