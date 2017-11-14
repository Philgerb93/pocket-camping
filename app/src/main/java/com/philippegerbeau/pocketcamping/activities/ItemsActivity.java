package com.philippegerbeau.pocketcamping.activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philippegerbeau.pocketcamping.Container;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.InteractiveEditText;
import com.philippegerbeau.pocketcamping.Item;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.adapters.ExpListAdapter;

import java.util.ArrayList;

public class ItemsActivity extends AppCompatActivity {
    InteractiveEditText intEditText;
    FloatingActionButton fab;
    LinearLayout inputLayout;

    ExpListAdapter expListAdapter;
    ArrayList<Container> containers = new ArrayList<>();
    ExpandableListView expListView;

    DatabaseReference fbStayRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        intEditText = findViewById(R.id.int_edit_text);
        fab = findViewById(R.id.fab);
        inputLayout = findViewById(R.id.input_layout);

        expListAdapter = new ExpListAdapter(this, containers);
        expListView = findViewById(R.id.exp_list_view);
        expListView.setAdapter(expListAdapter);

        /*expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                System.out.println("CLICKED");
                return true; // This way the expander cannot be collapsed
            }
        });*/

        fbStayRef = FirebaseDatabase.getInstance().getReference()
                .child("stays").child(Handler.user.getStayID());

        fbStayRef.child("containers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Container container = dataSnapshot.getValue(Container.class);
                if (container != null) {
                    container.setKey(dataSnapshot.getKey());
                    containers.add(container);
                    expListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Container container = dataSnapshot.getValue(Container.class);

                if (container != null) {
                    int i = 0;
                    while(!containers.get(i).getKey().equals(dataSnapshot.getKey())) {
                        i++;
                    }
                    containers.remove(i);
                    container.setKey(dataSnapshot.getKey());
                    containers.add(i, container);
                    expListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int i = 0;
                while(!containers.get(i).getKey().equals(dataSnapshot.getKey())) {
                    i++;
                }

                containers.remove(i);
                expListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void startInput(View view) {
        fab.setVisibility(View.INVISIBLE);
        inputLayout.setVisibility(View.VISIBLE);

        if (view.getId() == R.id.fab) {
            intEditText.setHint(R.string.new_container);
        } else {
            intEditText.setHint(R.string.new_item);
        }
        intEditText.requestFocus();
        showKeyboard();

    }

    public void submit(View view) {
        String input = intEditText.getText().toString();

        if (input.length() > 0) {
            if (expListAdapter.getContainerID() == null) {
                String key = fbStayRef.child("containers").push().getKey();
                fbStayRef.child("containers").child(key).setValue(new Container(input, key));
            } else {
                fbStayRef.child("containers").child(expListAdapter.getContainerID())
                        .child("items").push().setValue(new Item(input));
                expListAdapter.resetContainerID();
            }

            stopInput();
            hideKeyboard();
        }
    }

    public void stopInput() {
        inputLayout.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        intEditText.setText("");
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(intEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(intEditText.getWindowToken(), 0);
    }
}
