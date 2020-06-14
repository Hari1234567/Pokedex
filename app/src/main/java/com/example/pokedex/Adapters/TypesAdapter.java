package com.example.pokedex.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.MainActivity;
import com.example.pokedex.R;

import java.util.ArrayList;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.CustomViewHolder>{
    private ArrayList<String> typesNames;
    Context context;
    @NonNull
    @Override
    public TypesAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.typespanel, parent, false);

        TypesAdapter.CustomViewHolder customViewHolder = new TypesAdapter.CustomViewHolder(view);

        return customViewHolder;
    }
    public TypesAdapter(ArrayList<String> typesNames){
        this.typesNames=typesNames;
    }

    @Override
    public void onBindViewHolder(@NonNull TypesAdapter.CustomViewHolder holder, int position) {

        holder.typesText.setText(typesNames.get(position).toUpperCase());
        if(MainActivity.viewMode==3) {
            holder.showPokeBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity) holder.showPokeBut.getContext();
                    MainActivity.pokeCount = 0;
                    mainActivity.page = 0;
                    mainActivity.pokeNames.clear();
                    mainActivity.recyclerView.setAdapter(mainActivity.recyclerAdapter);
                    mainActivity.TypeFilterCall(-1, position,"");

                }
            });
        }if(MainActivity.viewMode==4){
            holder.showPokeBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity) holder.showPokeBut.getContext();
                    MainActivity.pokeCount = 0;
                    mainActivity.page = 0;
                    mainActivity.pokeNames.clear();
                    mainActivity.recyclerView.setAdapter(mainActivity.recyclerAdapter);
                    mainActivity.RegionFilterCall(-1,position,"");

                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return typesNames.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView typesText;
        Button showPokeBut;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            showPokeBut=(Button)itemView.findViewById(R.id.showPokemonsBut);
            typesText=(TextView)itemView.findViewById(R.id.typesText);


        }
    }
}
