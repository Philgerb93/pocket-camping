package com.philippegerbeau.pocketcamping.data;

public class Item implements ListItem {
    private String name;
    private boolean checked;

    public Item() {}

    public Item(String name) {
        this.name = name;
        checked = false;
    }

    public String getName() {
        return name;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
