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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.MainActivity;
import com.example.pokedex.PokeDetails;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.CustomViewHolder>  {
    private ArrayList<String> locationNames;
    Context context;
    @NonNull
    @Override
    public LocationAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locationpanel, parent, false);

        LocationAdapter.CustomViewHolder customViewHolder = new LocationAdapter.CustomViewHolder(view);

        return customViewHolder;
    }
    public LocationAdapter(ArrayList<String> locationNames){
           this.locationNames=locationNames;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.CustomViewHolder holder, int position) {

         holder.locationText.setText(locationNames.get(position).toUpperCase());

    }

    @Override
    public int getItemCount() {
        return locationNames.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
       TextView locationText;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            locationText=(TextView)itemView.findViewById(R.id.locationText);


        }
    }
}
