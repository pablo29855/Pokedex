package com.conoland.pokedex;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conoland.pokedex.models.Pokemon;

import java.util.ArrayList;

public class Detalles_PojkemonAdapter extends RecyclerView.Adapter<Detalles_PojkemonAdapter.ViewHolder> {

    private ArrayList<Pokemon>listaDetalles = new ArrayList<>();
    private Context context;
    private ListaPokemonAdapter parent;

    public Detalles_PojkemonAdapter(Context context, ListaPokemonAdapter parent) {
        this.context = context;
        this.parent = parent;
    }

    public  void adicionarDetallesPokemon(ArrayList<Pokemon> list) {
        listaDetalles.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Detalles_PojkemonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_etails_pokemon,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Detalles_PojkemonAdapter.ViewHolder holder, int position) {
    Pokemon p = listaDetalles.get(position);
    holder.name.setText(p.getName());
    holder.weight.setText("PESO: "+p.getWeight()+"Kg");
    Glide.with(holder.name.getContext()).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+p.getNumber()+".png")
            .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listaDetalles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView weight;
        TextView volver;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.fotoD);
            name= itemView.findViewById(R.id.nombreD);
            weight= itemView.findViewById(R.id.pesoD);
            volver=itemView.findViewById(R.id.volver);

           volver.setOnClickListener(view->{
               Activity activity=((Activity) context);
               RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
               final GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
               recyclerView.setLayoutManager(layoutManager);
               recyclerView.setAdapter(parent);


           });
        }
    }
}
