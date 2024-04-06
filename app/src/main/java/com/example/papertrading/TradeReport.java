package com.example.papertrading;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TradeReport extends AppCompatActivity {

    private String gain, gainPercent, annualGain, gainType, days;
    private TextView gainTV, gainPercentTV, annualGainTV, gainTypeTV, daysTV;
    private ConstraintLayout mLayout;
    private ImageView mImageView;
    private final String TAG = "Trade Report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trade_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reportMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(TAG, "TradeReport activity in");
        Intent intent = getIntent();
        gain = intent.getStringExtra("gain");
        gainPercent = intent.getStringExtra("gainPercent");
        annualGain = intent.getStringExtra("annualGain");
        days = intent.getStringExtra("days");
        if(gain.charAt(0)=='-')
            gainType="Loss";
        else gainType = "Profit";

        gainTV = findViewById(R.id.reportGainTextView);
        gainTypeTV = findViewById(R.id.reportGainTypeTextView);
        gainPercentTV = findViewById(R.id.reportGainPercentTextView);
        annualGainTV = findViewById(R.id.annualGainTextView);
        daysTV = findViewById(R.id.reportDaysTextView);
        mLayout = findViewById(R.id.reportMain);
        mImageView = findViewById(R.id.reportImageView);

        gainTV.setText(gain);
        gainTypeTV.setText(gainType);
        gainPercentTV.setText(gainPercent + "%");
        annualGainTV.setText(annualGain +"%");
        daysTV.setText("In " + days + " days");
        if(gainType=="Loss") {
            mLayout.setBackgroundColor(Color.parseColor("#AB0202"));
            mImageView.setImageDrawable(getDrawable(R.drawable.sad));
        }
    }
}