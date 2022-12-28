package com.conoland.pokedex.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

public class PokemonRespuesta {

    private ArrayList<Pokemon> results;

    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }
}

