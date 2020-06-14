package com.example.pokedex;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokeDetails extends AppCompatActivity {
    ScrollView detailsPanel;
    LinearLayout statsLayout;
    ArrayList<TextView> statViews;
    Intent dataIntent;
    Retrofit retrofit;
    PokeInterface pokeInterface;
    ProgressBar loadingBar;
    String parentSpecies;
    int evolveChainLength;
    IndividualData pokeData;
    String imageURL;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_details);
        retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build();
        pokeInterface = retrofit.create(PokeInterface.class);
        dataIntent=getIntent();
        statsLayout=(LinearLayout)findViewById(R.id.statsLayout);
        loadingBar=(ProgressBar)findViewById(R.id.loadingBar);
        evolveChainLength=0;
        Gson gson=new Gson();

        pokeData=gson.fromJson(dataIntent.getStringExtra("PokeDetailsJson"),IndividualData.class);



        loadingBar.setVisibility(View.INVISIBLE);
        Button shareBut=(Button)findViewById(R.id.shareBut);
        final File[] file=new File[1];
        final FileOutputStream[] fout=new FileOutputStream[1];
         Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.viewMode==5){
                    imageURL=pokeData.getSprites().getFront_default();

                }else{
                    imageURL=dataIntent.getStringExtra("picture");
                }
           Picasso.get().load(imageURL).into(new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                try {
                    file[0]=new File(PokeDetails.this.getCacheDir(),"pokemon.png");

                    fout[0]=new FileOutputStream(file[0]);

                bitmap.compress(Bitmap.CompressFormat.PNG,100,fout[0]);

                    fout[0].flush();
                    fout[0].close();
                    file[0].setReadable(true,false);


                }catch (Exception e){
                    e.printStackTrace();

                    Log.i("YATA","IOE2");
                }


            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


           if(imageURL!=null){
               try {
                   Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file[0]);
                   shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                   shareIntent.setType("img/png");
               }catch (Exception e){Toast.makeText(getApplicationContext(),"Sprite Unavailable",Toast.LENGTH_SHORT).show();}
               }else{
               shareIntent.setType("text/plain");
           }
           shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           shareIntent.putExtra(Intent.EXTRA_TEXT,"https://pokeapi.co/api/v2/pokemon/"+imageURL);
           startActivity(Intent.createChooser(shareIntent,"Share Pokemon: "+imageURL));


            }
        });

        statViews=new ArrayList<TextView>();

        TextView nameField=(TextView)findViewById(R.id.nameField);
        nameField.setText(dataIntent.getStringExtra("name").toUpperCase());
        for(int i=0;i<statsLayout.getChildCount();i++){
            try {
                statViews.add((TextView) statsLayout.getChildAt(i));
            }catch(Exception e){
                statViews.add(null);
            }
            }
        statViews.get(1).setText("BASE EXPERIENCE:     " + Integer.toString(pokeData.getBase_experience()));
        statViews.get(2).setText("HEIGHT:     " + Integer.toString(pokeData.getHeight()));
        statViews.get(3).setText("WEIGHT:     " + Integer.toString(pokeData.getWeight()));
        for (int i = 0; i < pokeData.getStats().size(); i++) {
            statViews.get(4 + i).setText(pokeData.getStats().get(i).getStat().getName().toUpperCase() + ":     " + Integer.toString(pokeData.getStats().get(i).getBase_stat()));
        }
        statViews.get(15).setText(pokeData.getName().toUpperCase());

        if(MainActivity.viewMode!=5) {
            Call<IndividualData.Species.SpeciesData> speciesDataCall = pokeInterface.getSpeciesData(pokeData.getSpecies().getName());
            speciesDataCall.enqueue(new Callback<IndividualData.Species.SpeciesData>() {
                @Override
                public void onResponse(Call<IndividualData.Species.SpeciesData> call, Response<IndividualData.Species.SpeciesData> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getEvolves_from_species() != null) {
                            statViews.get(13).setText(response.body().getEvolves_from_species().getSpeciesName().toUpperCase());
                            Call<IndividualData.Species.SpeciesData> speciesDataCall1 = pokeInterface.getSpeciesData(response.body().getEvolves_from_species().getSpeciesName());
                            speciesDataCall1.enqueue(new Callback<IndividualData.Species.SpeciesData>() {
                                @Override
                                public void onResponse(Call<IndividualData.Species.SpeciesData> call, Response<IndividualData.Species.SpeciesData> response) {
                                    if (response.body().getEvolves_from_species() != null) {
                                        statViews.get(11).setText(response.body().getEvolves_from_species().getSpeciesName().toUpperCase());
                                    } else {
                                        ((ViewGroup) statsLayout.getChildAt(12).getParent()).removeView(statsLayout.getChildAt(12));
                                        ((ViewGroup) statsLayout.getChildAt(11).getParent()).removeView(statsLayout.getChildAt(11));
                                    }

                                }

                                @Override
                                public void onFailure(Call<IndividualData.Species.SpeciesData> call, Throwable t) {

                                }
                            });
                        } else {
                            ((ViewGroup) statsLayout.getChildAt(14).getParent()).removeView(statsLayout.getChildAt(14));
                            ((ViewGroup) statsLayout.getChildAt(13).getParent()).removeView(statsLayout.getChildAt(13));
                            ((ViewGroup) statsLayout.getChildAt(12).getParent()).removeView(statsLayout.getChildAt(12));
                            ((ViewGroup) statsLayout.getChildAt(11).getParent()).removeView(statsLayout.getChildAt(11));

                        }
                    }
                }

                @Override
                public void onFailure(Call<IndividualData.Species.SpeciesData> call, Throwable t) {

                }
            });



        }else {

            if (dataIntent.getStringExtra("Father") != null) {
                statViews.get(13).setText(dataIntent.getStringExtra("Father").toUpperCase());
                if (dataIntent.getStringExtra("GrandFather") != null) {
                    statViews.get(11).setText(dataIntent.getStringExtra("GrandFather").toUpperCase());

                } else {
                    ((ViewGroup) statsLayout.getChildAt(12).getParent()).removeView(statsLayout.getChildAt(12));
                    ((ViewGroup) statsLayout.getChildAt(11).getParent()).removeView(statsLayout.getChildAt(11));
                }

            } else {

                ((ViewGroup) statsLayout.getChildAt(14).getParent()).removeView(statsLayout.getChildAt(14));
                ((ViewGroup) statsLayout.getChildAt(13).getParent()).removeView(statsLayout.getChildAt(13));
                ((ViewGroup) statsLayout.getChildAt(12).getParent()).removeView(statsLayout.getChildAt(12));
                ((ViewGroup) statsLayout.getChildAt(11).getParent()).removeView(statsLayout.getChildAt(11));

            }
        }
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            viewParams.setMargins(30, 30, 30, 30);
            TextView otherText;

            LinearLayout myLayout = (LinearLayout) findViewById(R.id.statsLayout);
            for (int i = 0; i < pokeData.getAbilities().size(); i++) {
                otherText = new TextView(getApplicationContext());
                otherText.setLayoutParams(viewParams);
                otherText.setGravity(Gravity.CENTER);
                otherText.setBackgroundColor(getResources().getColor(R.color.transparentblue));
                otherText.setTextColor(Color.YELLOW);

                otherText.setText(pokeData.getAbilities().get(i).getAbility().getName().toUpperCase());

                myLayout.addView(otherText, viewParams);
            }
            otherText = new TextView(getApplicationContext());
            otherText.setLayoutParams(viewParams);
            otherText.setTextSize(30);
            otherText.setGravity(Gravity.CENTER);
            otherText.setText("MOVES");
            otherText.setTypeface(getResources().getFont(R.font.steel));

            myLayout.addView(otherText, viewParams);
            for (int i = 0; i < pokeData.getMoves().size(); i++) {
                otherText = new TextView(getApplicationContext());
                otherText.setLayoutParams(viewParams);
                otherText.setGravity(Gravity.CENTER);
                otherText.setText(pokeData.getMoves().get(i).getMove().getName().toUpperCase());
                otherText.setBackgroundColor(getResources().getColor(R.color.transparentgreen));
                otherText.setTextColor(Color.YELLOW);
                myLayout.addView(otherText, viewParams);
            }






                ImageView pokeImage = (ImageView) findViewById(R.id.pokeImage);
        if(MainActivity.viewMode!=5) {
            Picasso.get().load(dataIntent.getStringExtra("picture")).into(pokeImage);
        }else{
            Picasso.get().load(pokeData.getSprites().getFront_default()).into(pokeImage);
        }
        detailsPanel=(ScrollView)findViewById(R.id.detailsPanel);

    }
}
