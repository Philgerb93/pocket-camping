package com.philippegerbeau.pocketcamping.data;

public class Ingredient {
    private String name;
    private boolean checked;

    @SuppressWarnings("unused")
    public Ingredient() {}

    public Ingredient(String name) {
        this.name = name;
        checked = false;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
