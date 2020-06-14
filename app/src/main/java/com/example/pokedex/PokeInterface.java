package com.example.pokedex;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeInterface {


      @GET("pokemon")
      Call<Pokemon> getPokemon(@Query("offset")int offset,@Query("limit")int limit);

      @GET("pokemon/{name}")
      Call<IndividualData> getIndividualData(@Path("name")String name);

      @GET("item")
      Call<Items> getItems(@Query("offset")int offset);

      @GET("item")
      Call<Items> getItems(@Query("offset")int offset,@Query("limit")int limit);

      @GET("item/{name}")
      Call<Items.ItemIndividualData> getItemIndividualData(@Path("name")String name);


      @GET("location")
      Call<Location> getLocations(@Query("offset")int offset,@Query("limit")int limit);

      @GET("type")
      Call<Types> getTypes(@Query("offset")int offset);

      @GET("type/{id}")
      Call<Types.IndividualType> getIndividualType(@Path("id")int id);

      @GET("pokemon-species/{name}")
      Call<IndividualData.Species.SpeciesData> getSpeciesData(@Path("name")String name);

      @GET("region")
      Call<Region> getRegions();

      @GET("generation/{id}")
      Call<Generation> getGeneration(@Path("id") int id);







}
