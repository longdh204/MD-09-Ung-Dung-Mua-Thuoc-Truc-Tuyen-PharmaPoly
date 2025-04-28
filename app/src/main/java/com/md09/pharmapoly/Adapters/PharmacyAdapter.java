package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Pharmacy;
import com.md09.pharmapoly.R;

import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.ViewHolder> {

    private Context context;
    private List<Pharmacy> pharmacies;

    public PharmacyAdapter(Context context, List<Pharmacy> pharmacies) {
        this.context = context;
        this.pharmacies = pharmacies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pharmacy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pharmacy pharmacy = pharmacies.get(position);
        holder.txtPharmacyName.setText(pharmacy.getName());
        holder.txtPharmacyLocation.setText(pharmacy.getLocation());
        holder.txtDistance.setText("Cách khoảng: " + String.format("%.2f", pharmacy.getDistance()) + " km");

        holder.btnDirections.setOnClickListener(v -> {
            // Mở Google Maps chỉ đường đến nhà thuốc
            String url = "https://www.google.com/maps/dir/?api=1&destination=" + pharmacy.getLatitude() + "," + pharmacy.getLongitude();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pharmacies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPharmacyName, txtPharmacyLocation, txtDistance;
        Button btnDirections;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPharmacyName = itemView.findViewById(R.id.txt_pharmacy_name);
            txtPharmacyLocation = itemView.findViewById(R.id.txt_pharmacy_location);
            txtDistance = itemView.findViewById(R.id.txt_distance);
            btnDirections = itemView.findViewById(R.id.btn_directions);
        }
    }
}