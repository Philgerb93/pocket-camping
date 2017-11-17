package com.philippegerbeau.pocketcamping.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.data.Friend;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FriendsListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Friend> friends;

    public FriendsListViewAdapter(Context context, ArrayList<Friend> friends) {
        this.context = context;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int i) {
        ArrayList<Friend> notAccepted = new ArrayList<>();
        ArrayList<Friend> accepted = new ArrayList<>();

        for (Friend friend : friends) {
            if (friend.isAccepted()) {
                accepted.add(friend);
            } else {
                notAccepted.add(friend);
            }
        }

        Collections.sort(notAccepted, new Comparator<Friend>() {
            @Override
            public int compare(Friend o1, Friend o2) {
                String nameO1 = Normalizer.normalize(o1.getName(), Normalizer.Form.NFD);
                String nameO2 = Normalizer.normalize(o2.getName(), Normalizer.Form.NFD);
                return nameO1.compareTo(nameO2);
            }
        });

        Collections.sort(accepted, new Comparator<Friend>() {
            @Override
            public int compare(Friend o1, Friend o2) {
                String nameO1 = Normalizer.normalize(o1.getName(), Normalizer.Form.NFD);
                String nameO2 = Normalizer.normalize(o2.getName(), Normalizer.Form.NFD);
                return nameO1.compareTo(nameO2);
            }
        });

        notAccepted.addAll(accepted);
        return notAccepted.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.friend, viewGroup, false);
        }

        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);
        ImageView profileImg = view.findViewById(R.id.profile_img);
        CardView cardView = view.findViewById(R.id.profile_layout);

        final Friend friend = (Friend) getItem(i);
        name.setText(friend.getName());
        email.setText(friend.getEmail().replace(',', '.'));

        if (friend.getPhotoUrl() != null) {
            profileImg.setImageURI(friend.getPhotoUrl());
        }

        if (!friend.isAccepted()) {
            view.findViewById(R.id.new_invite).setVisibility(View.VISIBLE);
            view.findViewById(R.id.options_layout).setVisibility(View.VISIBLE);

            Button decline = view.findViewById(R.id.decline);
            Button accept = view.findViewById(R.id.accept);

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(Handler.userID).child("friends")
                            .child(friend.getId()).removeValue();

                    Friend me = new Friend(Handler.username, Handler.email, null);
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(friend.getId()).child("friends")
                            .child(Handler.userID).setValue(me);
                }
            });

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(Handler.userID).child("friends")
                            .child(friend.getId()).child("accepted").setValue(true);
                }
            });
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(20, 2, 16, 4);
            cardView.requestLayout();
        }

        return view;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
