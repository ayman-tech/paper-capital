package com.example.papertrading;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderComplete extends AppCompatActivity {

    private ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_complete);
        layout = findViewById(R.id.orderReportMain);
        ViewCompat.setOnApplyWindowInsetsListener(layout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(type.equals("sell")) {
            int myColor = Color.parseColor("#A00000");
            layout.setBackgroundColor(myColor);
        }
        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(() -> {
            if(type.equals("buy")) {
                finish();
            }
            else{
                Intent reportIntent = new Intent(OrderComplete.this, TradeReport.class);
                reportIntent.putExtra("gain", intent.getStringExtra("gain"));
                reportIntent.putExtra("gainPercent", intent.getStringExtra("gainPercent"));
                reportIntent.putExtra("annualGain", intent.getStringExtra("annualGain"));
                reportIntent.putExtra("days", intent.getStringExtra("days"));
                startActivity(reportIntent);
            }
        }, 2000);
    }
}