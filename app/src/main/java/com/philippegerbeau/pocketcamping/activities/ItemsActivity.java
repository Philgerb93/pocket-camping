package com.philippegerbeau.pocketcamping.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philippegerbeau.pocketcamping.data.Alert;
import com.philippegerbeau.pocketcamping.data.Container;
import com.philippegerbeau.pocketcamping.Handler;
import com.philippegerbeau.pocketcamping.InteractiveEditText;
import com.philippegerbeau.pocketcamping.data.Item;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.adapters.ExpListAdapter;
import com.philippegerbeau.pocketcamping.data.ListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private InteractiveEditText intEditText;
    private FloatingActionButton fab;
    private LinearLayout inputLayout;
    private LinearLayout selectionLayout;
    private TextView title;

    private ExpListAdapter expListAdapter;
    private ArrayList<Container> containers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        intEditText = findViewById(R.id.int_edit_text);
        fab = findViewById(R.id.fab);
        inputLayout = findViewById(R.id.input_layout);
        selectionLayout = findViewById(R.id.selection_layout);
        title = findViewById(R.id.title);

        expListAdapter = new ExpListAdapter(this, containers);
        ExpandableListView expListView = findViewById(R.id.exp_list_view);
        expListView.setAdapter(expListAdapter);

        setAdapterListeners(expListView);
        setFirebaseListeners();
    }

    private void setAdapterListeners(final ExpandableListView expListView) {
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Item item = (Item) expListAdapter.getChild(groupPosition, childPosition);

                expListAdapter.setSelected((item == expListAdapter.getSelected()) ? null : item);
                if (expListAdapter.getSelected() != null) {
                    Container container = (Container) expListAdapter.getGroup(groupPosition);
                    expListAdapter.setSelectedGroup(container);
                }

                setToolbar();
                expListAdapter.notifyDataSetChanged();

                return true;
            }
        });

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Container container = (Container) expListAdapter.getGroup(groupPosition);

                expListAdapter.setSelected((container == expListAdapter.getSelected()) ? null : container);

                setToolbar();
                expListAdapter.notifyDataSetChanged();

                return true;
            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    Container container = (Container) expListAdapter.getGroup(groupPosition);
                    Item item = (Item) expListAdapter.getChild(groupPosition, childPosition);
                    Boolean checked = !item.isChecked();

                    for(Map.Entry<String, Item> entry : container.getItems().entrySet()) {
                        String key = entry.getKey();
                        Item value = entry.getValue();

                        if (value.getName().equals(item.getName())) {
                            Handler.fbStayRef.child("containers").child(container.getKey())
                                    .child("items").child(key).child("checked").setValue(checked);
                            break;
                        }
                    }
                    expListAdapter.notifyDataSetChanged();

                    return true;
                }

                return false;
            }
        });
    }

    private void setToolbar() {
        if (expListAdapter.getSelected() == null) {
            selectionLayout.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.items));
            title.setTextColor(Color.WHITE);
            toolbar.setBackgroundColor(ContextCompat.getColor(
                    ItemsActivity.this, R.color.colorPrimary));
            toolbar.getMenu().findItem(R.id.action_save).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_load).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_check).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_uncheck).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
        } else {
            selectionLayout.setVisibility(View.VISIBLE);
            title.setText(expListAdapter.getSelected().getName());
            title.setTextColor(ContextCompat.getColor(
                    ItemsActivity.this, R.color.colorTextPrimary));
            toolbar.setBackgroundColor(Color.WHITE);
            toolbar.getMenu().findItem(R.id.action_save).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_load).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_check).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_uncheck).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
        }
    }

    private void setFirebaseListeners() {
        Handler.fbStayRef.child("containers").addChildEventListener(new ChildEventListener() {
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
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                savePreset();
                return true;
            case R.id.action_load:
                loadPreset();
                return true;
            case R.id.action_check:
                checkAll();
                return true;
            case R.id.action_uncheck:
                uncheckAll();
                return true;
            case R.id.action_delete:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePreset() {
        if (containers.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        Map<String, Container> value = new HashMap<>();
                        for (Container container : containers) {
                            value.put(container.getKey(), container);
                        }

                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(Handler.userID).child("itemsPreset")
                                .setValue(value);
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.save))
                    .setMessage(getResources().getString(R.string.confirm_save))
                    .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                    .show();
        } else {
            Toast.makeText(this, getString(R.string.no_save), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadPreset() {
        final DatabaseReference fbUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(Handler.userID);

        fbUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("itemsPreset")) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                fbUserRef.child("itemsPreset")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemsActivity.this);
                    builder.setTitle(getResources().getString(R.string.load))
                            .setMessage(getResources().getString(R.string.confirm_load))
                            .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                            .show();
                } else {
                    Toast.makeText(ItemsActivity.this, getString(R.string.no_load),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void checkAll() {
        if (containers.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        Map<String, Container> value = new HashMap<>();

                        for (Container container : containers) {
                            for (Item item : container.getItemsList()) {
                                item.setChecked(true);
                            }
                            value.put(container.getKey(), container);
                        }

                        Handler.fbStayRef.child("containers").setValue(value);
                        Alert.log(Alert.CHECKED_ALL, Alert.CAT_ITEMS);
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.check_all))
                    .setMessage(getResources().getString(R.string.confirm_check))
                    .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                    .show();
        } else {
            Toast.makeText(this, getString(R.string.no_check), Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void uncheckAll() {
        if (containers.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        Map<String, Container> value = new HashMap<>();

                        for (Container container : containers) {
                            for (Item item : container.getItemsList()) {
                                item.setChecked(false);
                            }
                            value.put(container.getKey(), container);
                        }

                        Handler.fbStayRef.child("containers").setValue(value);
                        Alert.log(Alert.UNCHECKED_ALL, Alert.CAT_ITEMS);
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.uncheck_all))
                    .setMessage(getResources().getString(R.string.confirm_check))
                    .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                    .show();
        } else {
            Toast.makeText(this, getString(R.string.no_uncheck), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void deleteAll() {
        if (containers.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        Handler.fbStayRef.child("containers").removeValue();
                        Alert.log(Alert.DELETED_ALL, Alert.CAT_ITEMS);
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.delete_all))
                    .setMessage(getResources().getString(R.string.confirm_check))
                    .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                    .show();
        } else {
            Toast.makeText(this, getString(R.string.no_delete), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void startInput(View view) {
        fab.setVisibility(View.INVISIBLE);
        inputLayout.setVisibility(View.VISIBLE);

        if (expListAdapter.getSelected() == null) {
            if (view.getId() == R.id.fab) {
                intEditText.setHint(R.string.new_container);
            } else {
                intEditText.setHint(R.string.new_item);
            }
        } else {
            if (expListAdapter.getSelectedGroup() == null) {
                intEditText.setHint(R.string.edit_item);
            } else {
                intEditText.setHint(R.string.edit_container);
            }
            intEditText.setText(expListAdapter.getSelected().getName());
            intEditText.setSelection(intEditText.getText().toString().length());
        }

        intEditText.requestFocus();
        showKeyboard();
    }

    public void submit(View view) {
        String input = intEditText.getText().toString().trim();

        if (input.length() > 0) {
            ListItem selection = expListAdapter.getSelected();
            if (expListAdapter.getModContainer() == null && selection == null
                    || selection != null && selection.getClass() == Container.class) {
                if (containerExists(input)) {
                    Toast.makeText(this, getString(R.string.already_exists),
                            Toast.LENGTH_SHORT).show();
                } else if (selection == null){
                    String key = Handler.fbStayRef.child("containers").push().getKey();
                    Handler.fbStayRef.child("containers").child(key).setValue(new Container(input, key));
                    Alert.log(Alert.ADDED, Alert.CAT_ITEMS, input);
                } else {
                    String key = ((Container) selection).getKey();
                    Alert.log(Alert.RENAMED, Alert.CAT_ITEMS, selection.getName(), input);
                    Handler.fbStayRef.child("containers").child(key).child("name")
                            .setValue(input);
                    title.setText(input);
                }
            } else {
                if (expListAdapter.getModContainer() != null && itemExists(
                        input, expListAdapter.getModContainer())
                        || expListAdapter.getSelected() != null && itemExists(
                                input, expListAdapter.getSelectedGroup())) {
                    Toast.makeText(this, getString(R.string.already_exists),
                            Toast.LENGTH_SHORT).show();
                } else if (expListAdapter.getSelected() == null){
                    String containerID = expListAdapter.getModContainer().getKey();
                    Handler.fbStayRef.child("containers").child(containerID)
                            .child("items").push().setValue(new Item(input));
                    Alert.log(Alert.ADDED, Alert.CAT_ITEMS, input,
                            expListAdapter.getModContainer().getName());
                    expListAdapter.resetModContainer();
                } else {
                    Container container = expListAdapter.getSelectedGroup();
                    Item item = (Item) expListAdapter.getSelected();
                    for(Map.Entry<String, Item> entry : container.getItems().entrySet()) {
                        String key = entry.getKey();
                        Item value = entry.getValue();

                        if (value.getName().equals(item.getName())) {
                            Handler.fbStayRef.child("containers").child(container.getKey())
                                    .child("items").child(key).child("name").setValue(input);
                            Alert.log(Alert.RENAMED, Alert.CAT_ITEMS, item.getName(), input,
                                    container.getName());
                            break;
                        }
                    }
                    title.setText(input);
                }
            }

            stopInput();
            hideKeyboard();
        }
    }

    private boolean containerExists(String input) {
        for (Container container : containers) {
            if (container.getName().equals(input)) {
                return true;
            }
        }
        return false;
    }

    private boolean itemExists(String input, Container container) {
        for (Item item : container.getItemsList()) {
            if (item.getName().equals(input)) {
                return true;
            }
        }
        return false;
    }

    public void stopInput() {
        inputLayout.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        intEditText.setText("");
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(intEditText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(intEditText.getWindowToken(), 0);
        }
    }

    public void editSelection(View view) {


        startInput(view);
    }

    public void deleteSelection(View view) {
        if (expListAdapter.getSelected().getClass() == Container.class) {
            Container container = (Container) expListAdapter.getSelected();
            Handler.fbStayRef.child("containers").child(container.getKey()).removeValue();
            Alert.log(Alert.DELETED, Alert.CAT_ITEMS, container.getName());
        } else {
            Item item = (Item) expListAdapter.getSelected();
            Container container = expListAdapter.getSelectedGroup();

            for(Map.Entry<String, Item> entry : container.getItems().entrySet()) {
                String key = entry.getKey();
                Item value = entry.getValue();

                if (value.getName().equals(item.getName())) {
                    Handler.fbStayRef.child("containers").child(container.getKey())
                            .child("items").child(key).removeValue();
                    break;
                }
            }
            Alert.log(Alert.DELETED, Alert.CAT_ITEMS, item.getName(), container.getName());
            expListAdapter.setSelectedGroup(null);
        }
        expListAdapter.setSelected(null);
    }
}
