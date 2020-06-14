package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pokedex.Adapters.FavouritesAdapter;
import com.example.pokedex.Adapters.LocationAdapter;
import com.example.pokedex.Adapters.RecyclerAdapter;
import com.example.pokedex.Adapters.TypesAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button but;
    int id=0;
    ArrayList<String> pokeImagesURL=new ArrayList<String>();
    public ArrayList<String> pokeNames=new ArrayList<String>();
    public RecyclerAdapter recyclerAdapter;
    LocationAdapter locationAdapter;
    TypesAdapter typesAdapter;
    FavouritesAdapter favouritesAdapter;
    public RecyclerView recyclerView;
    Retrofit retrofit;
    PokeInterface pokeInterface;
    public static int viewMode=0;
    NavigationView navigationView;
    public static int pokeCount=0;
    boolean isLoading=false,isLastPage=false;
    public int page=0;
    LinearLayoutManager recyclerLayoutManager;
    ProgressBar loadingBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    ArrayList<String> totalNames;
    int actualOffset=-1,apparentOffset=0;
    ArrayList<String> totalLocations;
    ArrayList<String> totalItems;
    String searchText="";
    SwipeRefreshLayout refreshLayout;
    List<PokemonRoomData> favData;
    public boolean regionFiltermode=false;

    ArrayList<ArrayList<String>> pokeTypeMap;
    ArrayList<ArrayList<String>> pokeRegionMap;
    public static ArrayList<IndividualData> individualDataList;
    public static pokeDexDataBase pokeDataBase;




    public boolean typeFiltermode=false;
    int typeID=-1,regionID=-1;


    public void reload(){
        loadingBar.setVisibility(View.VISIBLE);
        pokeNames.clear();
        if(viewMode==0)
            totalNames.clear();
        if(viewMode==1)
            totalLocations.clear();
        if(viewMode==2)
            totalItems.clear();
        if(viewMode==3 && typeFiltermode){

            pokeTypeMap.get(typeID).clear();
        }
        if(viewMode==4&& regionFiltermode){
            pokeRegionMap.get(regionID).clear();
        }

        actualOffset=-1;
        apparentOffset=0;
        for(int i=0;i<pokeImagesURL.size();i++){
            pokeImagesURL.set(i,"https://github.com/Hari1234567/nothing/blob/master/blank.png");
            individualDataList.set(i,null);
        }
        retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build();
        pokeInterface = retrofit.create(PokeInterface.class);
        if(viewMode==3 && typeFiltermode) {

            Call<Types.IndividualType> individualTypeCall = pokeInterface.getIndividualType(typeID);

            individualTypeCall.enqueue(new Callback<Types.IndividualType>() {
                @Override
                public void onResponse(Call<Types.IndividualType> call, Response<Types.IndividualType> response) {
                    if (response.isSuccessful()) {
                        for (int j = 0; j < response.body().getPokemon().size(); j++) {
                            pokeTypeMap.get(typeID).add(response.body().getPokemon().get(j).getPokemonIndividual().getName());
                        }

                        TypeFilterCall(-1,typeID,searchText);

                    }
                }

                @Override
                public void onFailure(Call<Types.IndividualType> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });

        }
        if(viewMode==3 && !typeFiltermode){
            TypesAPICall(0);
        }
        if(viewMode==4 && regionFiltermode){
            Call<Generation> individualTypeCall = pokeInterface.getGeneration(regionID);

            individualTypeCall.enqueue(new Callback<Generation>() {
                @Override
                public void onResponse(Call<Generation> call, Response<Generation> response) {
                    if (response.isSuccessful()) {
                        for (int j = 0; j < response.body().getPokemon_species().size(); j++) {
                            pokeRegionMap.get(regionID).add(response.body().getPokemon_species().get(j).getName());
                        }

                        RegionFilterCall(-1,regionID,searchText);

                    }
                }

                @Override
                public void onFailure(Call<Generation> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });
        }
        if(viewMode==4 && !regionFiltermode){
            RegionAPICall();
        }
        if(viewMode==0) {
            Call<Pokemon> totalPokemonCall = pokeInterface.getPokemon(0,964);

            totalPokemonCall.enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            totalNames.add(response.body().getResult().get(i).getName());
                        }
                        APICall(-1,searchText);
                    }

                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });
        }
        if(viewMode==2) {
            Call<Location> totalLocationCall = pokeInterface.getLocations(0,781);

            totalLocationCall.enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        totalLocations.add(response.body().getResults().get(i).getName());
                    }
                    LocationAPICall(searchText);
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });
        }
        if(viewMode==1){
            Call<Items> itemsCall=pokeInterface.getItems(0,954);

            itemsCall.enqueue(new Callback<Items>() {
                @Override
                public void onResponse(Call<Items> call, Response<Items> response) {
                    if(response.isSuccessful()){
                        for(int i=0;i<response.body().getResults().size();i++){
                            totalItems.add(response.body().getResults().get(i).getName());
                        }
                        ItemAPICall(-1,searchText);
                    }
                }

                @Override
                public void onFailure(Call<Items> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });}




        if(viewMode==5){
            loadingBar.setVisibility(View.INVISIBLE);
        }
        refreshLayout.setRefreshing(false);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e){}
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        totalNames=new ArrayList<String>();
        individualDataList=new ArrayList<IndividualData>();

        favData=new ArrayList<PokemonRoomData>();

        pokeDataBase= Room.databaseBuilder(getApplicationContext(),pokeDexDataBase.class,"userdb").allowMainThreadQueries().build();
        totalLocations=new ArrayList<String>();
        totalItems=new ArrayList<String>();
        favouritesAdapter=new FavouritesAdapter(favData);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });


        pokeTypeMap=new ArrayList<ArrayList<String>>();
        pokeRegionMap=new ArrayList<ArrayList<String>>();



        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setEnabled(false);
        navigationView.getMenu().getItem(0).setChecked(true);





        loadingBar=(ProgressBar)findViewById(R.id.loadingBar);

        recyclerLayoutManager=new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        loadingBar.setVisibility(View.INVISIBLE);

        for(int i=0;i<964;i++){
            pokeImagesURL.add("https://github.com/Hari1234567/nothing/blob/master/blank.png");
            individualDataList.add(null);

        }
        pokeCount=0;
        recyclerAdapter=new RecyclerAdapter(pokeImagesURL,pokeNames);
        locationAdapter=new LocationAdapter(pokeNames);
        typesAdapter=new TypesAdapter(pokeNames);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.setLayoutManager(recyclerLayoutManager);
        pokeCount=20;

        retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build();
        pokeInterface = retrofit.create(PokeInterface.class);
        for(int i=0;i<20;i++){
            pokeTypeMap.add(new ArrayList<String>());
        }
        for(int i=0;i<7;i++){
            pokeRegionMap.add(new ArrayList<String>());
        }
        for(int i=0;i<20;i++){
            Call<Types.IndividualType> individualTypeCall=pokeInterface.getIndividualType(i+1);
            int finalI = i;
            individualTypeCall.enqueue(new Callback<Types.IndividualType>() {
                @Override
                public void onResponse(Call<Types.IndividualType> call, Response<Types.IndividualType> response) {
                    if(response.isSuccessful()){
                        for(int j=0;j<response.body().getPokemon().size();j++){
                            pokeTypeMap.get(finalI).add(response.body().getPokemon().get(j).getPokemonIndividual().getName());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Types.IndividualType> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });
        }

        for(int i=0;i<7;i++){
            Call<Generation> generationCall=pokeInterface.getGeneration(i+1);
            int finalI = i;
            generationCall.enqueue(new Callback<Generation>() {
                @Override
                public void onResponse(Call<Generation> call, Response<Generation> response) {
                    if(response.isSuccessful()){
                        for(int j=0;j<response.body().getPokemon_species().size();j++){
                            pokeRegionMap.get(finalI).add(response.body().getPokemon_species().get(j).getName());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Generation> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });
        }






        loadingBar.setVisibility(View.VISIBLE);
        Call<Pokemon> totalPokemonCall = pokeInterface.getPokemon(0,964);

        totalPokemonCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(response.isSuccessful()) {
                    for(int i=0;i<response.body().getResult().size();i++){
                        totalNames.add(response.body().getResult().get(i).getName());
                    }
                    APICall(-1,"");
                }

            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                printToast("Network issue, please reload");
            }
        });
        Call<Location> totalLocationCall = pokeInterface.getLocations(0,781);
        totalLocationCall.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                for(int i=0;i<response.body().getResults().size();i++){
                    totalLocations.add(response.body().getResults().get(i).getName());
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                printToast("Network issue, please reload");
            }
        });

        Call<Items> itemsCall=pokeInterface.getItems(0,954);

        itemsCall.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().getResults().size();i++){
                        totalItems.add(response.body().getResults().get(i).getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                printToast("Network issue, please reload");
            }
        });

        recyclerView.addOnScrollListener(new PaginationListener(recyclerLayoutManager) {
            @Override
            public boolean isLoading() {

                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public void loadMoreItems() {


                isLoading=true;
                if(viewMode==0) {
                    APICall(actualOffset,searchText);
                    apparentOffset++;
                }
                if(viewMode==1) {
                    ItemAPICall(actualOffset, searchText);
                    apparentOffset++;
                }
                if(viewMode==3) {


                    if(typeFiltermode){
                        TypeFilterCall(actualOffset,typeID,searchText);
                    }else {

                        TypesAPICall((++page) * 20);
                        apparentOffset++;
                    }
                }
                if(viewMode==4){

                    if(regionFiltermode){
                        RegionFilterCall(actualOffset,regionID,searchText);

                    }
                }
            }
        });


        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        recyclerAdapter.notifyDataSetChanged();
                        typesAdapter.notifyDataSetChanged();
                        locationAdapter.notifyDataSetChanged();
                        favouritesAdapter.notifyDataSetChanged();

                        break;
                    case ItemTouchHelper.RIGHT:
                        if ((MainActivity.viewMode == 0)||(MainActivity.viewMode==3&&typeFiltermode)||(MainActivity.viewMode==4&&regionFiltermode)) {
                            Gson gson = new Gson();
                            PokemonRoomData pokeData = new PokemonRoomData();
                            IndividualData individualData = individualDataList.get(viewHolder.getAdapterPosition());
                            Call<IndividualData.Species.SpeciesData> speciesDataCall = pokeInterface.getSpeciesData(individualData.getSpecies().getName());
                            pokeData.setSpecies(individualData.getSpecies().getName());
                            pokeData.setId(individualData.getId());
                            pokeData.setPokemonJson(gson.toJson(individualData));
                            pokeData.setName(pokeNames.get(viewHolder.getAdapterPosition()));
                            Picasso.get().load(individualData.getSprites().getFront_default()).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the bitmap object
                                    byte[] b = baos.toByteArray();
                                    pokeData.setImageByteCode(b);

                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });

                            speciesDataCall.enqueue(new Callback<IndividualData.Species.SpeciesData>() {
                                @Override
                                public void onResponse(Call<IndividualData.Species.SpeciesData> call, Response<IndividualData.Species.SpeciesData> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getEvolves_from_species() != null) {
                                            pokeData.setFatherspecies(response.body().getEvolves_from_species().getSpeciesName());
                                            Call<IndividualData.Species.SpeciesData> speciesDataCall1 = pokeInterface.getSpeciesData(response.body().getEvolves_from_species().getSpeciesName());
                                            speciesDataCall1.enqueue(new Callback<IndividualData.Species.SpeciesData>() {
                                                @Override
                                                public void onResponse(Call<IndividualData.Species.SpeciesData> call, Response<IndividualData.Species.SpeciesData> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().getEvolves_from_species() != null)
                                                            pokeData.setGrandfatherspecies(response.body().getEvolves_from_species().getSpeciesName());

                                                        try {
                                                            pokeDataBase.myroomInterface().addPokemon(pokeData);
                                                            printToast("Pokemon added to favourites");
                                                        } catch (SQLiteConstraintException s) {
                                                            printToast("Pokemon already in favourites");
                                                        }
                                                        recyclerAdapter.notifyDataSetChanged();

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<IndividualData.Species.SpeciesData> call, Throwable t) {
                                                    printToast("Network issue, please reload");
                                                }
                                            });
                                        } else {
                                            printToast("Pokemon added to favourites");
                                            try {
                                                pokeDataBase.myroomInterface().addPokemon(pokeData);
                                            }catch (SQLiteConstraintException s) {
                                                printToast("Pokemon already in favourites");
                                            }
                                            recyclerAdapter.notifyDataSetChanged();

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<IndividualData.Species.SpeciesData> call, Throwable t) {
                                    printToast("Network issue, please reload");
                                }
                            });
                        }else if(viewMode==5){
                            printToast("Pokemon removed from favourites");
                            pokeDataBase.myroomInterface().deletePokemon(favData.get(viewHolder.getAdapterPosition()));
                            favData=pokeDataBase.myroomInterface().getFavData();
                            favouritesAdapter=new FavouritesAdapter(favData);
                            recyclerView.setAdapter(favouritesAdapter);

                        }
                        if(MainActivity.viewMode==1){
                            recyclerAdapter.notifyDataSetChanged();
                        }
                        typesAdapter.notifyDataSetChanged();
                        locationAdapter.notifyDataSetChanged();
                        favouritesAdapter.notifyDataSetChanged();
                        break;
                }




            }
        };

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
    public void printToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    public void APICall(int offset,String target){

        for(int i=offset+1;i<totalNames.size();i++){

            if(totalNames.get(i).contains(target)) {
                pokeNames.add(totalNames.get(i));
                actualOffset = i;

                if (pokeNames.size() >= 20*(apparentOffset+1) ){

                    break;

                }
            }
        }
        for(int i=apparentOffset*20;i<pokeNames.size();i++){
            Call<IndividualData> pokeData=pokeInterface.getIndividualData(pokeNames.get(i));
            int finalI = i;
            pokeData.enqueue(new Callback<IndividualData>() {
                @Override
                public void onResponse(Call<IndividualData> call, Response<IndividualData> response) {
                    if(response.body().getSprites().getFront_default()==null){
                        pokeImagesURL.set(finalI,"https://github.com/Hari1234567/nothing/blob/master/blank.png");
                    }else
                        pokeImagesURL.set(finalI,response.body().getSprites().getFront_default());

                    individualDataList.set(finalI,response.body());
                    if(viewMode==0)
                        recyclerAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<IndividualData> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });
        }
        // printToast(actualOffset+" " +pokeNames.size());


        loadingBar.setVisibility((View.INVISIBLE));
        isLoading=false;



    }
    public void reset(){

        pokeNames.clear();
        typeID=-1;
        typeFiltermode=false;
    }
    public void TypeFilterCall(int offset,int typeID,String target){

        this.typeID = typeID;
        typeFiltermode = true;
        if(pokeTypeMap.get(typeID).size()==0) {
            Call<Types.IndividualType> pokeCall;

            pokeCall = pokeInterface.getIndividualType(typeID + 1);

            pokeCall.enqueue(new Callback<Types.IndividualType>() {
                @Override
                public void onResponse(Call<Types.IndividualType> call, Response<Types.IndividualType> response) {
                    if (!response.isSuccessful()) {

                        return;
                    }
                    Types.IndividualType individualType = response.body();
                    if (pokeNames.size() < individualType.getPokemon().size()) {
                        for (int i = 0; i < response.body().getPokemon().size(); i++) {
                            pokeTypeMap.get(typeID).add(response.body().getPokemon().get(i).getPokemonIndividual().getName());
                        }
                        for (int i = offset + 1; i < pokeTypeMap.get(typeID).size(); i++) {
                            if ( pokeTypeMap.get(typeID).get(i).contains(target)) {
                                pokeNames.add( pokeTypeMap.get(typeID).get(i));
                                actualOffset = i;

                                if (pokeNames.size() >= 20 * (apparentOffset + 1)) {

                                    break;

                                }
                            }
                        }


                        for (int i = apparentOffset * 20; i < pokeNames.size(); i++) {


                            Call<IndividualData> individualDataCall = pokeInterface.getIndividualData(pokeNames.get(i));

                            int finalI = i;
                            individualDataCall.enqueue(new Callback<IndividualData>() {
                                @Override
                                public void onResponse(Call<IndividualData> call, Response<IndividualData> response) {
                                    if (!response.isSuccessful()) {
                                        return;
                                    }


                                    pokeImagesURL.set(finalI, (response.body().getSprites().getFront_default()));
                                    if (pokeImagesURL.get(finalI) == null) {
                                        pokeImagesURL.set(finalI, "https://github.com/Hari1234567/nothing/blob/master/blank.png");

                                    }


                                    recyclerAdapter.notifyDataSetChanged();


                                }

                                @Override
                                public void onFailure(Call<IndividualData> call, Throwable t) {
                                    printToast("Network issue, please reload");
                                }
                            });
                        }

                    }




                }


                @Override
                public void onFailure(Call<Types.IndividualType> call, Throwable t) {
                    printToast("Network issue, please reload");
                }

            });
        }else{

            for(int i=offset+1;i< pokeTypeMap.get(typeID).size();i++){
                if(pokeTypeMap.get(typeID).get(i).contains(target)) {
                    pokeNames.add( pokeTypeMap.get(typeID).get(i));

                    actualOffset = i;

                    if (pokeNames.size() >= 20*(apparentOffset+1) ){

                        break;

                    }
                }
            }
            for(int i=apparentOffset*20;i<pokeNames.size();i++){
                Call<IndividualData> pokeData=pokeInterface.getIndividualData(pokeNames.get(i));
                int finalI = i;
                pokeData.enqueue(new Callback<IndividualData>() {
                    @Override
                    public void onResponse(Call<IndividualData> call, Response<IndividualData> response) {

                        if(response.isSuccessful()) {

                            if (response.body().getSprites().getFront_default() == null) {
                                pokeImagesURL.set(finalI, "https://github.com/Hari1234567/nothing/blob/master/blank.png");

                            } else {
                                pokeImagesURL.set(finalI, response.body().getSprites().getFront_default());

                            }

                            individualDataList.set(finalI,response.body());
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<IndividualData> call, Throwable t) {
                        printToast("Network issue, please reload");
                    }
                });

            }
            // printToast(actualOffset+" " +pokeNames.size());




        }
        loadingBar.setVisibility((View.INVISIBLE));
        isLoading = false;

    }

    public void RegionFilterCall(int offset,int regionID,String target){

        this.regionID = regionID;
        regionFiltermode = true;
        loadingBar.setVisibility(View.VISIBLE);
        if(pokeRegionMap.get(regionID).size()==0) {
            Call<Generation> pokeCall;

            pokeCall = pokeInterface.getGeneration(regionID + 1);

            pokeCall.enqueue(new Callback<Generation>() {
                @Override
                public void onResponse(Call<Generation> call, Response<Generation> response) {
                    if (!response.isSuccessful()) {

                        return;
                    }
                    Generation generation = response.body();
                    if (pokeNames.size() < generation.getPokemon_species().size()) {
                        for (int i = 0; i < response.body().getPokemon_species().size(); i++) {
                            pokeRegionMap.get(regionID).add(response.body().getPokemon_species().get(i).getName());
                        }
                        for (int i = offset + 1; i < pokeRegionMap.get(regionID).size(); i++) {
                            if ( pokeRegionMap.get(regionID).get(i).contains(target)) {
                                pokeNames.add( pokeRegionMap.get(regionID).get(i));
                                actualOffset = i;

                                if (pokeNames.size() >= 20 * (apparentOffset + 1)) {

                                    break;

                                }
                            }
                        }


                        for (int i = apparentOffset * 20; i < pokeNames.size(); i++) {


                            Call<IndividualData> individualDataCall = pokeInterface.getIndividualData(pokeNames.get(i));

                            int finalI = i;
                            individualDataCall.enqueue(new Callback<IndividualData>() {
                                @Override
                                public void onResponse(Call<IndividualData> call, Response<IndividualData> response) {
                                    if (!response.isSuccessful()) {
                                        return;
                                    }


                                    pokeImagesURL.set(finalI, (response.body().getSprites().getFront_default()));
                                    if (pokeImagesURL.get(finalI) == null) {
                                        pokeImagesURL.set(finalI, "https://github.com/Hari1234567/nothing/blob/master/blank.png");

                                    }
                                    loadingBar.setVisibility(View.INVISIBLE);

                                    recyclerAdapter.notifyDataSetChanged();


                                }

                                @Override
                                public void onFailure(Call<IndividualData> call, Throwable t) {
                                    printToast("Network issue, please reload");
                                }
                            });
                        }

                    }




                }


                @Override
                public void onFailure(Call<Generation> call, Throwable t) {
                    printToast("Network issue, please reload");
                }

            });
        }else{

            for(int i=offset+1;i< pokeRegionMap.get(regionID).size();i++){
                if(pokeRegionMap.get(regionID).get(i).contains(target)) {
                    pokeNames.add(pokeRegionMap.get(regionID).get(i));

                    actualOffset = i;

                    if (pokeNames.size() >= 20*(apparentOffset+1) ){

                        break;

                    }
                }
            }
            for(int i=apparentOffset*20;i<pokeNames.size();i++){
                Call<IndividualData> pokeData=pokeInterface.getIndividualData(pokeNames.get(i));
                int finalI = i;
                pokeData.enqueue(new Callback<IndividualData>() {
                    @Override
                    public void onResponse(Call<IndividualData> call, Response<IndividualData> response) {

                        if(response.isSuccessful()) {
                            loadingBar.setVisibility(View.INVISIBLE);
                            if (response.body().getSprites().getFront_default() == null) {
                                pokeImagesURL.set(finalI, "https://github.com/Hari1234567/nothing/blob/master/blank.png");

                            } else {
                                pokeImagesURL.set(finalI, response.body().getSprites().getFront_default());

                            }

                            individualDataList.set(finalI,response.body());
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<IndividualData> call, Throwable t) {
                        printToast("Network issue, please reload");
                    }
                });

            }
            // printToast(actualOffset+" " +pokeNames.size());




        }
        loadingBar.setVisibility((View.INVISIBLE));
        isLoading = false;


    }



    public void ItemAPICall(int offset, String target) {

        for (int i = offset + 1; i < totalItems.size(); i++) {
            if (totalItems.get(i).contains(target)) {
                pokeNames.add(totalItems.get(i));
                actualOffset = i;

                if (pokeNames.size() >= 20 * (apparentOffset + 1)) {

                    break;

                }
            }
        }

        for (int i = apparentOffset * 20; i < pokeNames.size(); i++) {
            Call<Items.ItemIndividualData> pokeData = pokeInterface.getItemIndividualData(pokeNames.get(i));
            int finalI = i;
            pokeData.enqueue(new Callback<Items.ItemIndividualData>() {
                @Override
                public void onResponse(Call<Items.ItemIndividualData> call, Response<Items.ItemIndividualData> response) {
                    if (response.body().getSprites().getSprite() == null) {
                        pokeImagesURL.set(finalI, "https://github.com/Hari1234567/nothing/blob/master/blank.png");
                    } else {
                        pokeImagesURL.set(finalI, response.body().getSprites().getSprite());
                    }
                    if(viewMode==1)
                        recyclerAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<Items.ItemIndividualData> call, Throwable t) {
                    printToast("Network issue, please reload");
                }
            });
        }
        loadingBar.setVisibility((View.INVISIBLE));
        isLoading=false;
    }

    public void LocationAPICall(String target){
        Call<Location> pokeCall;
        loadingBar.setVisibility(View.VISIBLE);

        for(int i=0;i<totalLocations.size();i++){
            if(totalLocations.get(i).contains(target)){
                pokeNames.add(totalLocations.get(i));
            }
        }
        if(viewMode==2)
            locationAdapter.notifyDataSetChanged();
        loadingBar.setVisibility(View.INVISIBLE);
    }
    public void TypesAPICall(int offset){
        Call<Types> pokeCall;

        pokeCount+=offset;
        loadingBar.setVisibility(View.VISIBLE);
        pokeCall = pokeInterface.getTypes(offset);
        pokeCall.enqueue(new Callback<Types>() {
            @Override
            public void onResponse(Call<Types> call, Response<Types> response) {
                if (!response.isSuccessful()) {

                    return;
                }
                loadingBar.setVisibility(View.INVISIBLE);
                Types items = response.body();
                //pokeNames.clear();
                for (int i = 0; i < items.getResults().size(); i++) {
                    pokeNames.add(items.getResults().get(i).getName());
                }

                if(viewMode==3)
                    typesAdapter.notifyDataSetChanged();


                loadingBar.setVisibility((View.INVISIBLE));
                isLoading=false;

            }

            @Override
            public void onFailure(Call<Types> call, Throwable t) {
                printToast("Network issue, please reload");
            }
        });
    }
    public void RegionAPICall(){
        Call<Region> pokeCall;


        loadingBar.setVisibility(View.VISIBLE);
        pokeCall = pokeInterface.getRegions();
        pokeCall.enqueue(new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                if (!response.isSuccessful()) {

                    return;
                }

                Region items = response.body();
                //pokeNames.clear();
                for (int i = 0; i < items.getResults().size(); i++) {
                    pokeNames.add(items.getResults().get(i).getName());
                }

                if(viewMode==4)
                    typesAdapter.notifyDataSetChanged();
                loadingBar.setVisibility(View.INVISIBLE);

                loadingBar.setVisibility((View.INVISIBLE));
                isLoading=false;

            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {
                printToast("Network issue, please reload");
            }
        });
    }

    void favouriteList(){
        favData=pokeDataBase.myroomInterface().getFavData();
        favouritesAdapter=new FavouritesAdapter(favData);
        if(favData!=null){


            recyclerView.setAdapter(favouritesAdapter);



            //printToast("daaad");
        }
        loadingBar.setVisibility(View.INVISIBLE);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        DrawerLayout mDrawerLayout;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        for(int i=0;i<navigationView.getMenu().size();i++){
            navigationView.getMenu().getItem(i).setChecked(false);
            navigationView.getMenu().getItem(i).setEnabled(true);
        }
        menuItem.setChecked(true);
        menuItem.setEnabled(false);
        mDrawerLayout.closeDrawers();
        if(menuItem.getItemId()==R.id.pokemonList){
            typeFiltermode=false;
            regionFiltermode=false;

            viewMode=0;
            reset();
            pokeNames.clear();
            for(int i=0;i<pokeImagesURL.size();i++){
                pokeImagesURL.set(i,"https://github.com/Hari1234567/nothing/blob/master/blank.png");
                individualDataList.set(i,null);
            }

            recyclerView.setAdapter(recyclerAdapter);
            pokeCount=0;
            page=0;
            APICall(-1,"");
            recyclerAdapter.notifyDataSetChanged();

        }
        if(menuItem.getItemId()==R.id.itemsList){
            typeFiltermode=false;
            regionFiltermode=false;
            viewMode=1;
            reset();
            pokeNames.clear();
            for(int i=0;i<pokeImagesURL.size();i++){
                pokeImagesURL.set(i, "https://github.com/Hari1234567/nothing/blob/master/blank.png");
                individualDataList.set(i,null);
            }
            pokeCount=0;
            page=0;
            recyclerView.setAdapter(recyclerAdapter);
            actualOffset=-1;
            apparentOffset=0;
            ItemAPICall(-1,"");
            recyclerAdapter.notifyDataSetChanged();


        }
        if(menuItem.getItemId()==R.id.locationList){
            typeFiltermode=false;
            regionFiltermode=false;
            pokeCount=0;
            reset();
            page=0;
            viewMode=2;
            pokeNames.clear();
            for(int i=0;i<pokeImagesURL.size();i++){
                pokeImagesURL.set(i, "https://github.com/Hari1234567/nothing/blob/master/blank.png");
                individualDataList.set(i,null);
            }
            recyclerView.setAdapter(locationAdapter);
            LocationAPICall("");
            locationAdapter.notifyDataSetChanged();

        }

        if(menuItem.getItemId()==R.id.typeList){
            pokeCount=0;
            reset();
            page=0;
            viewMode=3;
            pokeNames.clear();
            for(int i=0;i<pokeImagesURL.size();i++){
                pokeImagesURL.set(i, "https://github.com/Hari1234567/nothing/blob/master/blank.png");
                individualDataList.set(i,null);
            }
            recyclerView.setAdapter(typesAdapter);
            TypesAPICall(0);
            typesAdapter.notifyDataSetChanged();

        }
        if(menuItem.getItemId()==R.id.regionList){
            pokeCount=0;
            reset();
            page=0;
            viewMode=4;
            pokeNames.clear();
            for(int i=0;i<pokeImagesURL.size();i++){
                pokeImagesURL.set(i, "https://github.com/Hari1234567/nothing/blob/master/blank.png");
                individualDataList.set(i,null);
            }

            recyclerView.setAdapter(typesAdapter);

            RegionAPICall();

        }

        if(menuItem.getItemId()==R.id.favouritesList){
            pokeCount=0;
            reset();
            page=0;
            viewMode=5;
            pokeNames.clear();
            for(int i=0;i<pokeImagesURL.size();i++){
                pokeImagesURL.set(i, "https://github.com/Hari1234567/nothing/blob/master/blank.png");
                individualDataList.set(i,null);
            }


            favouriteList();
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onBackPressed() {
        if(viewMode==3) {
            if (!typeFiltermode)
                super.onBackPressed();
            else {
                typeFiltermode = false;
                pokeCount = 0;
                page = 0;
                pokeNames.clear();
                recyclerView.setAdapter(typesAdapter);

                actualOffset = -1;
                apparentOffset = 0;
                TypesAPICall(0);

            }
        }else if(viewMode==4){
            if (!regionFiltermode)
                super.onBackPressed();
            else {
                regionFiltermode = false;
                pokeCount = 0;
                page = 0;
                pokeNames.clear();
                recyclerView.setAdapter(typesAdapter);

                actualOffset = -1;
                apparentOffset = 0;
                RegionAPICall();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText=newText;
                actualOffset=-1;
                apparentOffset=0;
                pokeNames.clear();
                for(int i=0;i<pokeImagesURL.size();i++){
                    pokeImagesURL.set(i,"https://github.com/Hari1234567/nothing/blob/master/blank.png");
                    individualDataList.set(i,null);

                }
                if(viewMode==0)
                    APICall(-1,newText);
                if(viewMode==1)
                    ItemAPICall(-1,newText);
                if(viewMode==2)
                    LocationAPICall(newText);
                if(viewMode==3 && typeFiltermode){
                    TypeFilterCall(-1,typeID,newText);
                }
                if(viewMode==4 && regionFiltermode){
                    RegionFilterCall(-1,regionID,newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
