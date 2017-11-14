package com.philippegerbeau.pocketcamping.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Alert {
    private static final String DATE_FORMAT = "d MMMM yyyy - HH:mm";
    public static final int CREATED = 0;
    public static final int JOINED = 1;
    public static final int QUIT = 2;
    public static final int ADDED = 3;
    public static final int DELETED = 4;
    public static final int RENAMED = 5;
    public static final int CHECKED = 6;
    public static final int UNCHECKED = 7;
    public static final int CAT_STAY = 20;
    public static final int CAT_ITEMS = 21;
    public static final int CAT_MEALS = 22;

    private String name;
    private int action;
    private int category;
    private String item1;
    private String item2;
    private long date;

    public static void log(int action, int category, String item1) {
        Alert.log(action, category, item1, null);
    }

    public static void log(int action, int category, String item1, String item2) {
        DatabaseReference fbStayRef = FirebaseDatabase.getInstance().getReference()
                .child("stays").child(Handler.user.getStayID());

        Map<String, Object> value = new HashMap<>();
        value.put("name", Handler.user.getName());
        value.put("action", action);
        value.put("item1", item1);
        if (item2 != null) {
            value.put("item2", item2);
        }
        value.put("category", category);
        value.put("date", ServerValue.TIMESTAMP);

        fbStayRef.child("alerts").push().setValue(value);
    }

    public Alert() {}

    public String getName() {
        return name;
    }

    public int getAction() {
        return action;
    }

    public String getFormattedAction(Context context) {
        switch(action) {
            case CREATED:
                return " " + context.getResources().getString(R.string.created) + " ";
            case JOINED:
                return " " + context.getResources().getString(R.string.joined) + " ";
            case QUIT:
                return " " + context.getResources().getString(R.string.quit) + " ";
            case ADDED:
                return " " + context.getResources().getString(R.string.added) + " ";
            case DELETED:
                return " " + context.getResources().getString(R.string.deleted) + " ";
            case RENAMED:
                return " " + context.getResources().getString(R.string.renamed) + " ";
            case CHECKED:
                return " " + context.getResources().getString(R.string.checked) + " ";
            case UNCHECKED:
                return " " + context.getResources().getString(R.string.unchecked) + " ";
            default:
                return null;
        }
    }

    @Exclude
    public String getBridge(Context context) {
        if (action == ADDED) {
            return " " + context.getResources().getString(R.string.to) + " ";
        } else {
            return " " + context.getResources().getString(R.string.in) + " ";
        }
    }

    public String getItem1() {
        return item1;
    }

    public String getItem2() {
        return item2;
    }

    public long getDate() {
        return date;
    }

    @Exclude
    public String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public int getCategory() {
        return category;
    }

    @Exclude
    public int getCategoryImg() {
        switch(category) {
            case Alert.CAT_STAY:
                return R.drawable.stay_icon;
            case Alert.CAT_ITEMS:
                return R.drawable.items_icon;
            case Alert.CAT_MEALS:
                return R.drawable.meals_icon;
            default:
                return 0;
        }

    }
}
