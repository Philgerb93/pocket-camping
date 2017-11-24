package com.philippegerbeau.pocketcamping.data;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Meal {
    public static final int BREAKFAST = 0;
    public static final int DINNER = 1;
    public static final int SUPPER = 2;
    public static final int SNACK = 3;

    private String name;
    private int category;
    private Map<String, Ingredient> ingredients = new HashMap<>();
    private String key;

    @SuppressWarnings("unused")
    public Meal() {}

    public Meal(String name, int category, String key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    @Exclude
    public ArrayList<Ingredient> getIngredientsList() {
        return new ArrayList<>(ingredients.values());
    }

    public Map<String, Ingredient> getIngredients() {
        return ingredients;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public int getCheckedCount() {
        int checked = 0;
        for (Ingredient ingredient : getIngredientsList()) {
            if (ingredient.isChecked()) {
                checked++;
            }
        }
        return checked;
    }
}
