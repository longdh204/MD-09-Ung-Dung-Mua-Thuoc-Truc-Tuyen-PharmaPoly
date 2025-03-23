package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.md09.pharmapoly.Models.District;
import com.md09.pharmapoly.R;

import java.util.ArrayList;

public class SpinnerDistrictAdapter extends ArrayAdapter<District> {

    private Context context;
    private ArrayList<District> listDistricts;

    private TextView tv_name;

    public SpinnerDistrictAdapter(@NonNull Context context, int resource, ArrayList<District> districts) {
        super(context, resource,districts);
        this.context = context;
        this.listDistricts = districts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final District district = listDistricts.get(position);

        if (district != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(district.getDistrictName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final District district = listDistricts.get(position);

        if (district != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(district.getDistrictName());
        }

        return convertView;
    }
}
