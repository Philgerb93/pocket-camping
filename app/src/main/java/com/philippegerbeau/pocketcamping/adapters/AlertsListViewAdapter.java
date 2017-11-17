package com.philippegerbeau.pocketcamping.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philippegerbeau.pocketcamping.R;
import com.philippegerbeau.pocketcamping.data.Alert;

import java.util.ArrayList;

public class AlertsListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Alert> alerts;

    public AlertsListViewAdapter(Context context, ArrayList<Alert> alerts) {
        this.context = context;
        this.alerts = alerts;
    }

    @Override
    public int getCount() {
        return alerts.size();
    }

    @Override
    public Object getItem(int i) {
        return alerts.get(alerts.size() - (i+1));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alert, viewGroup, false);
        }

        TextView action = view.findViewById(R.id.action);
        TextView date = view.findViewById(R.id.date);
        ImageView category = view.findViewById(R.id.category_img);

        Alert alert = (Alert) getItem(i);
        action.setText("");
        setActionStr(alert, action);
        date.setText(alert.getFormattedDate());

        category.setImageResource(alert.getCategoryImg());

        return view;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    private void setActionStr(Alert alert, TextView action) {
        SpannableStringBuilder nameStr = new SpannableStringBuilder(alert.getName());
        nameStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, alert.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        action.append(nameStr);
        action.append(alert.getFormattedAction((context)));

        if (alert.getItem2() != null) {
            SpannableStringBuilder item1Str = new SpannableStringBuilder(alert.getItem1());
            item1Str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
                    0, alert.getItem1().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder item2Str = new SpannableStringBuilder(alert.getItem2().toUpperCase());
            item2Str.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.colorPrimaryDark)),
                    0, alert.getItem2().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            action.append(item1Str);
            action.append(alert.getBridge(context));
            action.append(item2Str);
        } else if (alert.getItem1() != null){
            SpannableStringBuilder item1Str = new SpannableStringBuilder(alert.getItem1().toUpperCase());
            item1Str.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.colorPrimaryDark)),
                    0, alert.getItem1().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            action.append(item1Str);
        }
    }
}
