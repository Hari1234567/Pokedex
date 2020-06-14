package com.example.pokedex;

import java.util.ArrayList;

public class Location {
    private ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }

    class Result{

        private String name;

        public String getName() {
            return name;
        }
    }
}
