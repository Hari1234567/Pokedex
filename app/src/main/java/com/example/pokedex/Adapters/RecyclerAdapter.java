package com.example.pokedex.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.pokedex.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {
    private ArrayList<String> pokeImagesURL;
    private ArrayList<String> pokeNames;

    Context context;
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_panel, parent, false);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }
    public RecyclerAdapter(ArrayList<String>pokeImagesURL,ArrayList<String>pokeNames){

        this.pokeImagesURL=pokeImagesURL;

        this.pokeNames=pokeNames;

    }



    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

            if (pokeImagesURL.get(position) == null) {

                return;
            }
            if (!pokeImagesURL.get(position).isEmpty())
                Picasso.get().load(pokeImagesURL.get(position)).into(holder.pokeImage);

            holder.imageURL = pokeImagesURL.get(position);
            holder.name = pokeNames.get(position);
            holder.pokeText.setText(pokeNames.get(position).toUpperCase());
            MainActivity mainActivity = (MainActivity) holder.detailsBut.getContext();
            if (MainActivity.viewMode != 0) {
                holder.detailsBut.setVisibility(View.INVISIBLE);
            } else {
                holder.detailsBut.setVisibility(View.VISIBLE);
            }

            if ((MainActivity.viewMode == 3 && mainActivity.typeFiltermode )||(MainActivity.viewMode==4&&mainActivity.regionFiltermode)) {
                holder.detailsBut.setVisibility(View.VISIBLE);
            }



    }

    @Override
    public int getItemCount() {
        return pokeNames.size();
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
                    i.putExtra("picture", imageURL);
                    i.putExtra("name",name);
                    Gson gson=new Gson();
                    i.putExtra("PokeDetailsJson",gson.toJson(MainActivity.individualDataList.get(getAdapterPosition())));

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)itemView.getContext(),(View)pokeImage,"imagetransit");
                    itemView.getContext().startActivity(i,options.toBundle());

                }
            });
        }
    }
}
