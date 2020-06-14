package com.example.pokedex.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.IndividualData;
import com.example.pokedex.MainActivity;
import com.example.pokedex.PokeDetails;
import com.example.pokedex.PokemonRoomData;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends  RecyclerView.Adapter<FavouritesAdapter.CustomViewHolder>{
    private List<PokemonRoomData> pokemonRoomData;

    Context context;
    @NonNull
    @Override
    public FavouritesAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_panel, parent, false);

        FavouritesAdapter.CustomViewHolder customViewHolder = new FavouritesAdapter.CustomViewHolder(view);

        return customViewHolder;
    }
    public FavouritesAdapter(List<PokemonRoomData> individualData){

             pokemonRoomData=individualData;
    }





    @Override
    public void onBindViewHolder(@NonNull FavouritesAdapter.CustomViewHolder holder, int position) {

       if(pokemonRoomData.get(position).getImageByteCode()!=null){
           Bitmap bitmap= BitmapFactory.decodeByteArray(pokemonRoomData.get(position).getImageByteCode(),0,pokemonRoomData.get(position).getImageByteCode().length);

           holder.pokeImage.setImageBitmap(bitmap);

       }

        holder.pokeText.setText(pokemonRoomData.get(position).getName().toUpperCase());


    }

    @Override
    public int getItemCount() {
        return pokemonRoomData.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView pokeImage;
        TextView pokeText;
        RelativeLayout pokeLayout;
        Button detailsBut;
        public String imageURL;
        public String name;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            pokeImage = itemView.findViewById(R.id.pokeImage);
            pokeText = itemView.findViewById(R.id.pokeName);
            pokeLayout = itemView.findViewById(R.id.pokeLayout);
            detailsBut = itemView.findViewById(R.id.detailsBut);


            detailsBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(itemView.getContext(), PokeDetails.class);
                    i.putExtra("PokeDetailsJson",pokemonRoomData.get(getAdapterPosition()).getPokemonJson());
                    i.putExtra("Father",pokemonRoomData.get(getAdapterPosition()).getFatherspecies());
                    i.putExtra("GrandFather",pokemonRoomData.get(getAdapterPosition()).getGrandfatherspecies());
                    i.putExtra("Species",pokemonRoomData.get(getAdapterPosition()).getSpecies());
                    i.putExtra("name",pokemonRoomData.get(getAdapterPosition()).getName());

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)itemView.getContext(),(View)pokeImage,"imagetransit");
                    itemView.getContext().startActivity(i,options.toBundle());

                }
            });
        }
    }
}
