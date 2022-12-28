package com.conoland.pokedex;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conoland.pokedex.models.Pokemon;
import com.conoland.pokedex.models.PokemonRespuesta;
import com.conoland.pokedex.pokeapi.PokeapiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListaPokemonAdapter extends RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder>{

        private ArrayList<Pokemon>dataset;
        ArrayList<Pokemon>listaOriginal;
        private Context context;

       public ListaPokemonAdapter(Context context){
           this.context= context;
           dataset = new ArrayList<>();
           listaOriginal= new ArrayList<>();
           listaOriginal.addAll(dataset);
       }
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListaPokemonAdapter.ViewHolder holder, int position) {
         Pokemon p = dataset.get(position);
         holder.nombre.setText(p.getName());
         holder.order = p.getNumber();
         Glide.with(holder.nombre.getContext()).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+p.getNumber()+".png")
                    .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).
                    into(holder.foto);
        }
        //CODIGO ENCARGADO DE FILTRAR LA BUSQUEDA DE POKEMON
        public void filtrado(String buscar){

           if(buscar.length()== 0){
               dataset.clear();
               dataset.addAll(listaOriginal);
           }else{
               if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                   List<Pokemon>colletion=dataset.stream().filter(i -> i.getName().toLowerCase().contains(buscar.toLowerCase()))
                           .collect(Collectors.toList());
                   dataset.clear();
                   dataset.addAll(colletion);
               }else{
                   for(Pokemon c:listaOriginal){
                       if(c.getName().toLowerCase().contains(buscar.toLowerCase())){
                          dataset.add(c);
                       }
                   }
               }
           }
           notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        public void adicionarListaPokemon(ArrayList<Pokemon> listaPokemon) {
           dataset.addAll(listaPokemon);
           listaOriginal.addAll(dataset);
           notifyDataSetChanged();
        }

        public class  ViewHolder extends RecyclerView.ViewHolder{
            private ImageView foto;
            private TextView nombre;
            private int order;
            private TextView volver;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                foto = (ImageView) itemView.findViewById(R.id.foto);
                nombre = (TextView) itemView.findViewById(R.id.nombre);
                volver = itemView.findViewById(R.id.volver);

                foto.setOnClickListener(view -> {
                    Activity parentActivity = ((Activity) context);

                    Detalles_PojkemonAdapter adapter = new Detalles_PojkemonAdapter(context, ListaPokemonAdapter.this);
                    datosPokemon(adapter, order);
                    parentActivity.findViewById(R.id.buscar).setVisibility(View.GONE);
                    RecyclerView recyclerView = ((RecyclerView) parentActivity.findViewById(R.id.recyclerView));
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);
                });
            }
        }

    private void datosPokemon(Detalles_PojkemonAdapter adapter, int pokemonNum)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/").
                addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeapiService service= retrofit.create(PokeapiService.class);
        Call<Pokemon> pokemonRespuestaCall=service.datos(pokemonNum);

        pokemonRespuestaCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(response.isSuccessful()){
                    ArrayList<Pokemon> list= new ArrayList<>();
                    list.add(response.body());
                    adapter.adicionarDetallesPokemon(list);
                }else {
                    Log.e(this.getClass().getSimpleName(), "onResponse" + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onResponse" +t.getMessage());
            }
        });
        }
    }


