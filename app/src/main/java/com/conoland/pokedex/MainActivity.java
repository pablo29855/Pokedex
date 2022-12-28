package com.conoland.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.conoland.pokedex.models.Pokemon;
import com.conoland.pokedex.models.PokemonRespuesta;
import com.conoland.pokedex.pokeapi.PokeapiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private RecyclerView recyclerDetalles;
    private ListaPokemonAdapter listaPokemonAdapter;
    private Detalles_PojkemonAdapter detalles_pojkemonAdapter;
    private static final String TAG = "POKEDEX";
    private int offset;
    private boolean aptoParaCargar;
    SearchView buscar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RELACIONES
        recyclerView = (RecyclerView ) findViewById(R.id.recyclerView);
        buscar= findViewById(R.id.buscar);
        buscar.setOnQueryTextListener(this);

        //METODOS PARA INFLAR VISTA PRINCIPAL
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCOunt = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (aptoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCOunt) {
                            aptoParaCargar = false;
                            offset +=20;
                            obtenerDatos(offset);
                        }
                    }
                }
            }
        });
        //METODO QUE TRAE LA LISTA DE POKEMONES Y CONVIERTE EL FORMATO JSON
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/").
                addConverterFactory(GsonConverterFactory.create())
                .build();
        aptoParaCargar = true;
        offset = 0;
        obtenerDatos(offset);

    }

    //METODOS PARA TRAER LISTA DE POKEMON
    private void obtenerDatos(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon(20, offset);

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoParaCargar = true;
                if (response.isSuccessful()) {
                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();
                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);

                } else {
                    Log.e(TAG, "onResponse" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, "onResponse" + t.getMessage());
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String buscar) {
        listaPokemonAdapter.filtrado(buscar);
        return false;
    }
}