package com.example.pokedex;

import java.util.ArrayList;

public class Generation {
    private ArrayList<Species> pokemon_species;

    public ArrayList<Species> getPokemon_species() {
        return pokemon_species;
    }

    class Species{
        private String name;

        public String getName() {
            return name;
        }
    }
}
