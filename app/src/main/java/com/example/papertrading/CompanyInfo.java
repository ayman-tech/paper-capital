package com.example.papertrading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CompanyInfo extends AppCompatActivity {

    private TextView priceTV, codeTV, changePercentTV, changeNoTV;
    private TextView dayLowTV, dayHighTV, yearHighTV, yearLowTV;
    private TextView peRatioTV, marCapTV, prevCloseTV;
    private ProgressBar dayPB, yearPB;
    private Button buyBT, sellBT;
    private String price, changeNo, changePercent, dayRange, yearRange, marCap, peRatio;
    private String code, prevClose, dayLow, dayHigh, yearLow, yearHigh;
    String TAG = "Company Info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        code = getIntent().getStringExtra("code");
        Log.d(TAG, "Company Info in");

        priceTV = findViewById(R.id.priceTextView);
        codeTV = findViewById(R.id.codeTextView);
        changePercentTV = findViewById(R.id.changePercentTextView);
        changeNoTV = findViewById(R.id.changeNoTextView);

        dayLowTV = findViewById(R.id.dayLowTextView);
        dayHighTV = findViewById(R.id.dayHighTextView);
        dayPB = findViewById(R.id.compInfoDayProgressBar);
        yearHighTV = findViewById(R.id.yearHighTextView);
        yearLowTV = findViewById(R.id.yearLowTextView);
        yearPB = findViewById(R.id.compInfoYearProgressBar);

        peRatioTV = findViewById(R.id.peRatioTextView);
        marCapTV = findViewById(R.id.marketCapTextView);
        prevCloseTV = findViewById(R.id.previousCloseTextView);
        buyBT = findViewById(R.id.compInfoBuy);
        sellBT = findViewById(R.id.compInfoSell);

        try {
            getCurrentData();

            codeTV.setText(code);
            priceTV.setText(price);
            changeNoTV.setText(changeNo);
            changePercentTV.setText(changePercent);

            dayLowTV.setText(dayLow);
            dayHighTV.setText(dayHigh);
            yearLowTV.setText(yearLow);
            yearHighTV.setText(yearHigh);

            dayPB.setMax((int) Double.parseDouble(dayHigh.replace(",","")));
            dayPB.setMin((int) Double.parseDouble(dayLow.replace(",","")));
            dayPB.setProgress((int) Double.parseDouble(price.replace(",","")));
            yearPB.setMax((int) Double.parseDouble(yearHigh.replace(",","")));
            yearPB.setMin((int) Double.parseDouble(yearLow.replace(",","")));
            yearPB.setProgress((int) Double.parseDouble(price.replace(",","")));

            marCapTV.setText(marCapManip(marCap));
            peRatioTV.setText(peRatio);
            prevCloseTV.setText(prevClose);
        }
        catch (Exception e){
            Log.e("TAG", "Failed");
            e.printStackTrace();
        }

        buyBT.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CartBuy.class);
            intent.putExtra("code", code);
            intent.putExtra("changeNo", changeNo);
            intent.putExtra("changePercent", changePercent);
            intent.putExtra("price", price);
            startActivity(intent);
        });
        sellBT.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CartSell.class);
            intent.putExtra("code", code);
            intent.putExtra("changeNo", changeNo);
            intent.putExtra("changePercent", changePercent);
            intent.putExtra("price", price);
            startActivity(intent);
        });
    }
    private String marCapManip(String marCap){
        if(marCap.charAt(marCap.length()-1)=='T'){
            Log.d(TAG, "Enter T edit");
            marCap = marCap.replace("T", "");
            Log.d(TAG, "rm T : " + marCap);
            Double mar_cap = Double.parseDouble(marCap);
            Log.d(TAG, "Double of marcap : "+mar_cap);
            mar_cap = mar_cap*100000;
            int tmp = (int) (mar_cap/1);
            marCap = "" + tmp + " Cr";
        }
        return marCap;
    }
    private void getCurrentData(){
        String url = "https://finance.yahoo.com/quote/" + code + ".NS";
        Log.d(TAG, url);
        Connection connection = Jsoup.connect(url);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(document!=null){
            Log.d(TAG, "Jsoup connection successful");
        }
        else Log.d(TAG, "Jsoup connection failed");

        price = document.selectXpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/fin-streamer[1]").first().text();
        changeNo = document.selectXpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/fin-streamer[2]").first().text();
        changePercent = document.selectXpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/fin-streamer[3]").first().text();
        dayRange = document.selectXpath("//*[@id=\"quote-summary\"]/div[1]/table/tbody/tr[5]/td[2]").first().text();
        yearRange = document.selectXpath("//*[@id=\"quote-summary\"]/div[1]/table/tbody/tr[6]/td[2]").first().text();
        marCap = document.selectXpath("//*[@id=\"quote-summary\"]/div[2]/table/tbody/tr[1]/td[2]").first().text();
        peRatio = document.selectXpath("//*[@id=\"quote-summary\"]/div[2]/table/tbody/tr[3]/td[2]").first().text();
        prevClose = document.selectXpath("//*[@id=\"quote-summary\"]/div[1]/table/tbody/tr[1]/td[2]").first().text();
        dayLow = dayRange.split(" - ")[0];
        dayHigh = dayRange.split(" - ")[1];
        yearLow = yearRange.split(" - ")[0];
        yearHigh = yearRange.split(" - ")[1];
    }
}