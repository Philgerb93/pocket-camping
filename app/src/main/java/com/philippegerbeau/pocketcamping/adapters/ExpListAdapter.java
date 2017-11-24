package com.philippegerbeau.pocketcamping.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.philippegerbeau.pocketcamping.data.Container;
import com.philippegerbeau.pocketcamping.data.Item;
import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.activities.ItemsActivity;
import com.philippegerbeau.pocketcamping.data.ListItem;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ExpListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Container> containers;

    private ListItem selected;
    private Container selectedGroup;
    private Container modifiedContainer;

    public ExpListAdapter(Context context, ArrayList<Container> containers) {
        this.context = context;
        this.containers = containers;
    }

    @Override
    public int getGroupCount() {
        return containers.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return ((Container)getGroup(i)).getItems().size();
    }

    @Override
    public Object getGroup(int i) {
        Collections.sort(containers, new Comparator<Container>() {
            @Override
            public int compare(Container o1, Container o2) {
                String nameO1 = Normalizer.normalize(o1.getName(), Normalizer.Form.NFD);
                String nameO2 = Normalizer.normalize(o2.getName(), Normalizer.Form.NFD);
                return nameO1.compareTo(nameO2);
            }
        });
        return containers.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        Container container = (Container) getGroup(i);

        Collections.sort(container.getItemsList(), new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                String nameO1 = Normalizer.normalize(o1.getName(), Normalizer.Form.NFD);
                String nameO2 = Normalizer.normalize(o2.getName(), Normalizer.Form.NFD);
                return nameO1.compareTo(nameO2);
            }
        });

        return container.getItemsList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemParentView = inflater.inflate(R.layout.item_parent, viewGroup, false);

        final Container container = (Container) getGroup(i);
        TextView name = itemParentView.findViewById(R.id.name);
        name.setText(container.getName());

        ImageView add = itemParentView.findViewById(R.id.add);

        ExpandableListView elv = ((ItemsActivity) context).findViewById(R.id.exp_list_view);
        elv.expandGroup(i);

        if (selected != null && selected == container) {
            itemParentView.setBackgroundColor(ContextCompat.getColor(
                    context, R.color.colorPrimaryLight));
            add.setVisibility(View.GONE);
        } else {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifiedContainer = container;
                    ((ItemsActivity) context).startInput(v);
                }
            });
        }

        return itemParentView;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemChildView = inflater.inflate(R.layout.item_child, viewGroup, false);

        TextView name = itemChildView.findViewById(R.id.name);
        Item item = (Item) getChild(i, i1);
        name.setText(item.getName());

        if (selected != null && selected == item) {
            itemChildView.setBackgroundColor(ContextCompat.getColor(
                    context, R.color.colorPrimaryLight));
        }

        if (item.isChecked()) {
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return itemChildView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void resetModContainer() {
        modifiedContainer = null;
    }

    public Container getModContainer() {
        return modifiedContainer;
    }

    public void setSelected(ListItem selected) {
        this.selected = selected;
    }

    public ListItem getSelected() {
        return selected;
    }

    public void setSelectedGroup(Container selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public Container getSelectedGroup() {
        return selectedGroup;
    }
}
