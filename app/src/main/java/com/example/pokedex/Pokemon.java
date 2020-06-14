package com.example.pokedex;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Pokemon {
    private int count;
    private String next;
    private String previous = null;
    private ArrayList<Result> results=new ArrayList<Result>();

    public ArrayList<Result> getResult() {
        return results;
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }


}
class Result{
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
