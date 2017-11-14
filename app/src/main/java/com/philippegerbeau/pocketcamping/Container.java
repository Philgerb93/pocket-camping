package com.philippegerbeau.pocketcamping;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Container {
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

    public ArrayList<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
