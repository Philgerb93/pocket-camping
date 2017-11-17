package com.philippegerbeau.pocketcamping.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.InteractiveEditText;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.activities.HomeActivity;
import com.philippegerbeau.pocketcamping.adapters.FriendsListViewAdapter;
import com.philippegerbeau.pocketcamping.data.Friend;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendsFragment extends Fragment {
    private ArrayList<Friend> friends;

    InteractiveEditText intEditText;
    FloatingActionButton fab;
    LinearLayout inputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        intEditText = view.findViewById(R.id.int_edit_text);
        fab = view.findViewById(R.id.friends_fab);
        inputLayout = view.findViewById(R.id.input_layout);

        final ListView listView = view.findViewById(R.id.list_view);
        final TextView noFriends = view.findViewById(R.id.no_friends);

        Handler.fbUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("friends")) {
                            noFriends.setVisibility(View.INVISIBLE);

                            friends = new ArrayList<>();
                            final FriendsListViewAdapter adapter = new FriendsListViewAdapter(getActivity(), friends);
                            listView.setAdapter(adapter);

                            Handler.fbUserRef.child("friends")
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Friend friend = dataSnapshot.getValue(Friend.class);

                                            if (friend != null) {
                                                friend.setId(dataSnapshot.getKey());
                                                friends.add(friend);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                            Friend friend = dataSnapshot.getValue(Friend.class);

                                            if (friend != null) {
                                                int i = 0;
                                                while (!friends.get(i).getId().equals(dataSnapshot.getKey())) {
                                                    i++;
                                                }
                                                friends.remove(i);
                                                friend.setId(dataSnapshot.getKey());
                                                friends.add(i, friend);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                                                int i = 0;
                                                while(!friends.get(i).getId().equals(dataSnapshot.getKey())) {
                                                    i++;
                                                }

                                                friends.remove(i);
                                                adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            noFriends.setVisibility(View.VISIBLE);
                        }

                        /*if (dataSnapshot.hasChild("invites")) {
                            Query query = FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(Handler.user.getUserID())
                                    .child("invites").orderByKey().limitToLast(1);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childSnap: dataSnapshot.getChildren()) {
                                        String name = childSnap.child("name").getValue(String.class);
                                        inviteText.setText(getResources().getString(R.string.new_invite));
                                        inviteText.append(" " + name);
                                        invitePanel.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            invitePanel.setVisibility(View.GONE);
                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }

    @SuppressWarnings("unused")
    public void addFriend(View view) {
        ((HomeActivity) getActivity()).hideNav();
        fab.setVisibility(View.INVISIBLE);
        inputLayout.setVisibility(View.VISIBLE);
        intEditText.requestFocus();
        showKeyboard();
    }

    @SuppressWarnings("unused")
    public void submit(View view) {
        final String input = intEditText.getText().toString().trim()
                .replace('.', ',');

        if (validEmail(input) && !input.equals(Handler.email)) {
            FirebaseDatabase.getInstance().getReference().child("emails")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(input)) {
                                final String friendID = dataSnapshot.child(input).getValue(String.class);

                                Handler.fbUserRef.child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (friendID != null && dataSnapshot.hasChild(friendID)) {
                                            Toast.makeText(getActivity(), getString(R.string.already_friend),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            sendInvite(friendID);

                                            Toast.makeText(getActivity(), getString(R.string.invitation_sent),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.no_email),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            Toast.makeText(getActivity(), getString(R.string.invitation_failed),
                    Toast.LENGTH_SHORT).show();
        }

        stopInput();
        hideKeyboard();
    }

    private boolean validEmail(String email) {
        String regex = "\\S+@\\S+,\\S+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendInvite(String friendID) {
        DatabaseReference fbFriendRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(friendID);

        Friend invite = new Friend(Handler.username, Handler.email, Handler.photoUrl);
        fbFriendRef.child("friends").child(Handler.userID).setValue(invite);
    }

    public void stopInput() {
        ((HomeActivity) getActivity()).showNav();
        inputLayout.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        intEditText.setText("");
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(intEditText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(intEditText.getWindowToken(), 0);
        }
    }
}
