package com.example.pokedex;

import java.net.PortUnreachableException;
import java.util.ArrayList;

public class Types {
    private ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }

    class Result{
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
    class IndividualType{
        private ArrayList<Pokemon> pokemon;

        public ArrayList<Pokemon> getPokemon() {
            return pokemon;
        }

        class Pokemon{
            private Poke pokemon;

            public Poke getPokemonIndividual() {
                return pokemon;
            }

            class Poke{
                     private String name;

                     public String getName() {
                         return name;
                     }
                 }
        }
    }
}
