package com.md09.pharmapoly.ui.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.md09.pharmapoly.Adapters.PharmacyAdapter;
import com.md09.pharmapoly.Models.Pharmacy;
import com.md09.pharmapoly.R;


import java.util.ArrayList;
import java.util.List;

public class PharmacyMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Pharmacy> pharmacies; // Danh sách các nhà thuốc
    private static final int LOCATION_REQUEST_CODE = 1000;
    private RecyclerView recyclerView;
    private PharmacyAdapter pharmacyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_map);
        EdgeToEdge.enable(this);

        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Khởi tạo SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        // Thiết lập RecyclerView cho hướng ngang
        recyclerView = findViewById(R.id.recycler_view_pharmacies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Thiết lập nút tìm nhà thuốc gần nhất
        Button btnFindNearestPharmacy = findViewById(R.id.btn_find_nearest_pharmacy);
        btnFindNearestPharmacy.setOnClickListener(v -> {
            findNearestPharmacy();
        });

        // Khởi tạo danh sách các nhà thuốc
        pharmacies = new ArrayList<>();
        addPharmacies();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Cấp quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

        // Hiển thị các nhà thuốc trên bản đồ
        displayPharmaciesOnMap();
    }

    private void addPharmacies() {
        // Thêm các nhà thuốc vào danh sách
        pharmacies.add(new Pharmacy("Nhà thuốc PharmaPoly cơ sở a", "", 0.0, 21.029, 105.8545));
        pharmacies.add(new Pharmacy("Nhà thuốc PharmaPoly cơ sở b", "", 0.0, 21.032, 105.8560));
        pharmacies.add(new Pharmacy("Nhà thuốc PharmaPoly cơ sở c", "", 0.0, 21.034, 105.8580));
        pharmacies.add(new Pharmacy("Nhà thuốc PharmaPoly cơ sở d", "", 0.0, 21.036, 105.8600));
        pharmacies.add(new Pharmacy("Nhà thuốc PharmaPoly cơ sở e", "", 0.0, 21.038, 105.8620));

        // Cập nhật RecyclerView
        pharmacyAdapter = new PharmacyAdapter(this, pharmacies);
        recyclerView.setAdapter(pharmacyAdapter);
    }

    private void displayPharmaciesOnMap() {
        for (Pharmacy pharmacy : pharmacies) {
            LatLng pharmacyLocation = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pharmacyLocation).title(pharmacy.getName()));
        }
    }

    private void getCurrentLocation() {
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa được cấp quyền, yêu cầu quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return; // Thoát khỏi phương thức nếu chưa có quyền
        }

        // Lấy vị trí hiện tại
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        updatePharmacyDistances(currentLocation);
                    } else {
                        Toast.makeText(this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePharmacyDistances(LatLng currentLocation) {
        for (Pharmacy pharmacy : pharmacies) {
            LatLng pharmacyLocation = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
            double distance = calculateDistance(currentLocation, pharmacyLocation);
            pharmacy.setDistance(distance); // Cập nhật khoảng cách
        }
        pharmacyAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
    }

    private void findNearestPharmacy() {
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return; // Thoát khỏi phương thức nếu chưa có quyền
        }

        // Lấy vị trí hiện tại
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        LatLng nearestPharmacy = null;
                        double nearestDistance = Double.MAX_VALUE;

                        // Tính toán khoảng cách đến từng nhà thuốc
                        for (Pharmacy pharmacy : pharmacies) {
                            LatLng pharmacyLocation = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
                            double distance = calculateDistance(currentLocation, pharmacyLocation);
                            if (distance < nearestDistance) {
                                nearestDistance = distance;
                                nearestPharmacy = pharmacyLocation;
                            }
                        }

                        // Di chuyển bản đồ đến nhà thuốc gần nhất
                        if (nearestPharmacy != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestPharmacy, 15));
                            mMap.addMarker(new MarkerOptions().position(nearestPharmacy).title("Nhà thuốc gần nhất"));
                        }
                    } else {
                        Toast.makeText(this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private double calculateDistance(LatLng start, LatLng end) {
        final int R = 6371; // Bán kính trái đất (km)
        double latDistance = Math.toRadians(end.latitude - start.latitude);
        double lngDistance = Math.toRadians(end.longitude - start.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude)) *
                        Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // khoảng cách (km)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
}