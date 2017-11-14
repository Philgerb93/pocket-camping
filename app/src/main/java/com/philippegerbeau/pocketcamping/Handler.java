package com.philippegerbeau.pocketcamping;

import com.philippegerbeau.pocketcamping.data.User;

public class Handler {
    public static User user;

    public static void init() {
        user = new User();
    }

}
