package com.philippegerbeau.pocketcamping.data;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class Friend {
    private String name;
    private String email;
    private Uri photoUrl;
    private boolean accepted;
    private String id;

    @SuppressWarnings("unused")
    public Friend() {}

    public Friend(String name, String email, Uri photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        accepted = false;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
