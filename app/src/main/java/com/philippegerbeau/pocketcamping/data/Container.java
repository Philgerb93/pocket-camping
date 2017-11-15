package com.philippegerbeau.pocketcamping.data;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Container implements ListItem {
    private String name;
    private Map<String, Item> items = new HashMap<>();
    private String key;

    public Container() {}

    public Container(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    @Exclude
    public ArrayList<Item> getItemsList() {
        return new ArrayList<>(items.values());
    }

    public Map<String, Item> getItems() {
        return items;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
