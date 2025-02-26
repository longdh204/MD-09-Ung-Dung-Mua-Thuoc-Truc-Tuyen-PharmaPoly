package com.md09.pharmapoly.ui.view.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.md09.pharmapoly.R;

public class Onboarding_Screen_2 extends AppCompatActivity {

    private TextView tv_sub_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding_screen_2);

        initUI();

        ApplyStyleText();
    }
    private void ApplyStyleText() {
        String text = getString(R.string.quality_healthcare_access);

        SpannableString spannableString = new SpannableString(text);

        int startPharma = text.indexOf("Pharma");
        int endPharma = startPharma + "Pharma".length();
        Typeface typefaceStart = ResourcesCompat.getFont(this, R.font.be_vietnam_pro_extrabold);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.blue_CB7)), startPharma, endPharma, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            spannableString.setSpan(new TypefaceSpan(typefaceStart), startPharma, endPharma, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        int startPoly = text.indexOf("Poly");
        int endPoly = startPoly + "Poly".length();
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.red_757)), startPoly, endPoly, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_sub_title.setText(spannableString);
    }
    private void initUI() {
        tv_sub_title = findViewById(R.id.tv_sub_title);
    }
}