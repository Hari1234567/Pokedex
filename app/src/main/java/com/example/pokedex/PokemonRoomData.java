package com.example.pokedex;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pokemons")
public class PokemonRoomData {
    @PrimaryKey
    int id;
    private String pokemonJson;
     private String species;
     private String fatherspecies;
     private String grandfatherspecies;
     private String name;
    @ColumnInfo(name = "imagebyte",typeAffinity = ColumnInfo.BLOB)
    private byte[] imageByteCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPokemonJson(String pokemonJson) {
        this.pokemonJson = pokemonJson;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setFatherspecies(String fatherspecies) {
        this.fatherspecies = fatherspecies;
    }

    public void setGrandfatherspecies(String grandfatherspecies) {
        this.grandfatherspecies = grandfatherspecies;
    }


    public String getPokemonJson() {
        return pokemonJson;
    }

    public String getSpecies() {
        return species;
    }

    public String getFatherspecies() {
        return fatherspecies;
    }

    public String getGrandfatherspecies() {
        return grandfatherspecies;
    }


    public byte[] getImageByteCode() {
        return imageByteCode;
    }

    public void setImageByteCode(byte[] imageByteCode) {
        this.imageByteCode = imageByteCode;
    }
}
