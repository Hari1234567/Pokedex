package com.example.pokedex;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Items {
    private ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }

    class Result{
       private String name;
       private String url;

        public String getName() {
            return name;
        }

        public String getURL() {
            return url;
        }
    }

    class ItemIndividualData{
        private Sprite sprites;

        public Sprite getSprites() {
            return sprites;
        }

        class Sprite{
            @SerializedName("default")
            private String sprite;

            public String getSprite() {
                return sprite;
            }
        }



    }
}
