package com.steamedpears.comp3004.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AssetMap extends HashMap<String, Integer> {

    /**
     * A helper method to calculate the sum of exactly two asset maps
     * @param first an asset map
     * @param last another asset map
     * @return an asset map corresponding to the sum of the two asset maps
     */
    public static AssetMap sum(AssetMap first, AssetMap last){
        Collection<AssetMap> col = new ArrayList<AssetMap>();
        col.add(first);
        col.add(last);
        return sum(col);
    }

    /**
     * Calculates the asset map corresponding to the sum of given asset maps
     * @param assets the collection of asset maps to sum
     * @return the sum of the asset maps, as an asset map
     */
    public static AssetMap sum(Collection<AssetMap> assets){
        AssetMap result = new AssetMap();
        for(AssetMap assetMap: assets){
            result.add(assetMap);
        }

        return result;
    }

    /**
     * A helper method to calculate the result of subtracting one asset map from another
     * @param first the asset map from which to be subtracted
     * @param last the asset map to subtract
     * @return the asset map result of subtracting last from first
     */
    public static AssetMap difference(AssetMap first, AssetMap last){
        AssetMap result = new AssetMap();
        for(String key: first.keySet()){
            int difference = first.get(key) - last.get(key);
            if(difference>0){
                result.put(key, difference);
            }
        }
        return result;
    }

    public AssetMap(){}

    public AssetMap(JsonObject obj){
        if(obj!=null){
            Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
            for(Map.Entry<String, JsonElement> entry: entrySet){
                put(entry.getKey(), entry.getValue().getAsInt());
            }
        }
    }

    public AssetMap(JsonObject obj, String key){
        this(obj.has(key) ? obj.getAsJsonObject(key) : new JsonObject());
    }

    @Override
    public Integer get(Object key){
        if(containsKey(key)){
            return super.get(key);
        }else{
            return 0;
        }
    }

    @Override
    public Integer put(String key, Integer value){
        if(value<0){
            value = 0;
        }
        if(value == 0){
            int oldValue = get(key);
            remove(key);
            return oldValue;
        }
        return super.put(key, value);
    }

    @Override
    public boolean containsKey(Object key){
        return get(key) != 0;
    }

    /**
     * Adds the values of the given assetMap to this one
     * @param assetMap The map to add
     */
    private void add(AssetMap assetMap) {
        for(String key: assetMap.keySet()){
            add(key, assetMap.get(key));
        }
    }

    /**
     * Adds the specified amount to the assetMap
     * @param key The key to add the value at
     * @param value The amount to add to the assetMap
     */
    private void add(String key, Integer value) {
        if(containsKey(key)){
            put(key, get(key) + value);
        }else{
            put(key, value);
        }
    }

    /**
     * Adds one of the specified type to the assetMap
     * @param key The key to add the value at
     */
    private void add(String key){
        add(key, 1);
    }

    /**
     * Subtracts the values of the given assetMap from this one
     * @param assetMap The assetMap to subtract
     */
    private void subtract(AssetMap assetMap){
        for(String key: assetMap.keySet()){
            subtract(key, assetMap.get(key));
        }
    }

    /**
     * Subtracts the value from the assetMap
     * @param key The key to subtract the value at
     * @param value The value to subtract
     */
    private void subtract(String key, Integer value) {
        put(key, get(key)-value);
    }

    /**
     * Subtracts one from the assetMap
     * @param key The key to subtract from
     */
    public void subtract(String key) {
        subtract(key, 1);
    }

    /**
     * Checks if the given choices of assets can be used to pay the given cost
     * @param choices list of sets of assets which can be used to pay the cost
     * @return true if choices can be used to pay cost
     */
    public boolean existsValidChoices(List<AssetSet> choices){
        return recursiveSearchForValidChoices(choices, 0);
    }

    private boolean recursiveSearchForValidChoices(List<AssetSet> choices, int index){
        Set<String> choice = choices.get(index);
        //loop over every asset in this choice
        boolean everRecursed = false;
        for(String asset: choice){
            //if this asset would help pay for the cost, try it
            if(containsKey(asset)){
                everRecursed = true;
                subtract(asset);
                //see if we've found a solution
                boolean foundSolution = true;
                for(int value: values()){
                    if(value>0){
                        foundSolution = false;
                        break;
                    }
                }
                //if that didn't solve it, recurse on the next choice
                if(!foundSolution && index+1<choices.size()){
                    foundSolution = recursiveSearchForValidChoices(choices, index+1);
                }
                //if we found a solution, return true; otherwise put the asset back and try the next asset in the choice
                add(asset);
                if(foundSolution){
                    return true;
                }
            }
        }
        //this asset is totally useless, go around it!
        if(!everRecursed && index+1<choices.size()){
            return recursiveSearchForValidChoices(choices, index+1);
        }
        //if we've gotten to here, no choice in this subtree works
        return false;
    }

}
