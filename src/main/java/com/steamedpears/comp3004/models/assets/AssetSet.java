package com.steamedpears.comp3004.models.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class AssetSet extends HashSet<String> {

    /**
     * Creates an empty AssetSet
     */
    public AssetSet(){}

    /**
     * Creates an AssetSet that matches the array that is a child of the given JSON object,
     * or an empty one if none exists
     * @param obj the object to get the array from
     * @param key the key to get the array from the object
     */
    public AssetSet (JsonObject obj, String key){
        this(obj.has(key) ? obj.getAsJsonArray(key) : new JsonArray());
    }

    /**
     * Creates an AssetSet that matches the given JSON array
     * @param arr the JSON array to match
     */
    public AssetSet (JsonArray arr){
        for(JsonElement entry: arr){
            add(entry.getAsString());
        }
    }
}
