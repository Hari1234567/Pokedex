package com.example.pokedex;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Region {
    private ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }

    class Result{
        private String url;
        private String name;
        public String getName(){
            return name;
        }
        private int getID(){
            return url.charAt(url.length()-1)-'0';
        }


    }


}
