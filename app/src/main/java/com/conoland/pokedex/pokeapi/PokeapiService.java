package com.conoland.pokedex.pokeapi;


import com.conoland.pokedex.models.Pokemon;
import com.conoland.pokedex.models.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonRespuesta>obtenerListaPokemon(@Query("limit") int limit,@Query("offset") int offset);

    @GET("pokemon/{id}")
    Call<Pokemon>datos(@Path("id") int id);
}
