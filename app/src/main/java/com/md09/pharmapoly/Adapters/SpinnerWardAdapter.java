package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.md09.pharmapoly.Models.Ward;
import com.md09.pharmapoly.R;

import java.util.ArrayList;

public class SpinnerWardAdapter extends ArrayAdapter<Ward> {

    private Context context;
    private ArrayList<Ward> listWards;

    private TextView tv_name;

    public SpinnerWardAdapter(@NonNull Context context, int resource, ArrayList<Ward> wards) {
        super(context, resource,wards);
        this.context = context;
        this.listWards = wards;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Ward ward = listWards.get(position);

        if (ward != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(ward.getWardName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Ward ward = listWards.get(position);

        if (ward != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(ward.getWardName());
        }

        return convertView;
    }
}
