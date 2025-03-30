package com.conoland.pokedex
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.conoland.pokedex.models.PokemonRespuesta
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var retrofit: Retrofit
    private lateinit var recyclerView: RecyclerView
    private lateinit var listaPokemonAdapter: ListaPokemonAdapter
    private lateinit var buscar: SearchView

    private var offset = 0
    private var aptoParaCargar = true
    private var posicionActual: Int = 0  // Nueva variable para guardar la posición

    companion object {
        private const val TAG = "POKEDEX"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        buscar = findViewById(R.id.buscar)
        buscar.setOnQueryTextListener(this)

        listaPokemonAdapter = ListaPokemonAdapter(this)
        recyclerView.adapter = listaPokemonAdapter
        recyclerView.setHasFixedSize(true)

        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (aptoParaCargar && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        aptoParaCargar = false
                        offset += 20
                        obtenerDatos(offset)
                    }
                }
            }
        })

        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        obtenerDatos(offset)
    }

    override fun onPause() {
        super.onPause()
        posicionActual = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onResume() {
        super.onResume()
        recyclerView.layoutManager?.scrollToPosition(posicionActual)
    }

    private fun obtenerDatos(offset: Int) {
        val service = retrofit.create(PokeapiService::class.java)
        val pokemonRespuestaCall = service.obtenerListaPokemon(20, offset)

        pokemonRespuestaCall.enqueue(object : Callback<PokemonRespuesta> {
            override fun onResponse(call: Call<PokemonRespuesta>, response: Response<PokemonRespuesta>) {
                aptoParaCargar = true
                if (response.isSuccessful) {
                    val listaPokemon = response.body()?.results ?: arrayListOf()
                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon)
                } else {
                    Log.e(TAG, "Error en la respuesta: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<PokemonRespuesta>, t: Throwable) {
                aptoParaCargar = true
                Log.e(TAG, "Error en la conexión: ${t.message}")
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        listaPokemonAdapter.filtrado(newText.orEmpty())
        return false
    }
}

