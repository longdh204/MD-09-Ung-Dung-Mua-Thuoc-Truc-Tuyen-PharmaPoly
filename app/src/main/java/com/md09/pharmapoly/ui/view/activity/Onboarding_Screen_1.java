package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.md09.pharmapoly.R;

public class Onboarding_Screen_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding_screen_1);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent= new Intent(Onboarding_Screen_1.this, ManHinh3.class);
//                startActivity(intent);
//                finish();
//            }
//        },2000);
    }
}