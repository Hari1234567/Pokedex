package com.example.pokedex;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface roomInterface {
    @Insert
    public void addPokemon(PokemonRoomData pokemonRoomData);

    @Query("select * from  pokemons")
    public List<PokemonRoomData> getFavData();

    @Delete
    public void deletePokemon(PokemonRoomData pokemonRoomData);
}
