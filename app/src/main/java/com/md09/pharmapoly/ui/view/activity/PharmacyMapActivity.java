package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.md09.pharmapoly.R;

public class PharmacyMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_map);

        // Khởi tạo SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Cập nhật vị trí bản đồ mặc định (ví dụ: tọa độ Hà Nội)
        LatLng hanoi = new LatLng(21.0285, 105.8542); // Vị trí mặc định
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoi, 12));

        // Thêm các nhà thuốc vào bản đồ
        LatLng pharmacy1 = new LatLng(21.029, 105.8545); // Ví dụ một nhà thuốc
        mMap.addMarker(new MarkerOptions().position(pharmacy1).title("Nhà thuốc A"));

        LatLng pharmacy2 = new LatLng(21.032, 105.8560); // Ví dụ nhà thuốc thứ 2
        mMap.addMarker(new MarkerOptions().position(pharmacy2).title("Nhà thuốc B"));

        LatLng pharmacy3 = new LatLng(31.032, 140.8560); // Ví dụ nhà thuốc thứ 2
        mMap.addMarker(new MarkerOptions().position(pharmacy2).title("Nhà thuốc C"));

        LatLng pharmacy4 = new LatLng(41.032, 150.8560); // Ví dụ nhà thuốc thứ 2
        mMap.addMarker(new MarkerOptions().position(pharmacy2).title("Nhà thuốc C"));
    }
}

