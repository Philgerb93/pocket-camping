package com.philippegerbeau.pocketcamping.data;

public class Item implements ListItem {
    private String name;
    private boolean checked;

    @SuppressWarnings("unused")
    public Item() {}

    public Item(String name) {
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
