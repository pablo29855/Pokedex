package com.conoland.pokedex
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.conoland.pokedex.models.Pokemon
import com.conoland.pokedex.models.PokemonRespuesta
import retrofit2.http.Query

interface PokeapiService {
    @GET("pokemon")
    fun obtenerListaPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PokemonRespuesta>

    @GET("pokemon/{id}")
    fun datos(@Path("id") id: Int): Call<Pokemon>
}
