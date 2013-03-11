package com.steamedpears.comp3004.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class AssetSet extends HashSet<String> {

    public AssetSet(){}

    public AssetSet (JsonObject obj, String key){
        this(obj.has(key) ? obj.getAsJsonArray(key) : new JsonArray());
    }

    public AssetSet (JsonArray arr){
        for(JsonElement entry: arr){
            add(entry.getAsString());
        }
    }
}
