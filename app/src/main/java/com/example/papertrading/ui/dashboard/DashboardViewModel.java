package com.example.papertrading.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.lifecycle.ViewModel;

import com.example.papertrading.PortfolioListData;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DashboardViewModel extends ViewModel {

//    private final MutableLiveData<String> mText;
    private ArrayList<PortfolioListData> portfolioListData;
    private Context context;
    private ArrayList<String> orderCode, orderPrice, orderQuant, orderDate;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private final String TAG = "Dashboard ViewModel";
    private String price, changeNo, changePercent;
    private Double invested;
    public DashboardViewModel() {
        portfolioListData = null;
    }
    public String getPortfolioStockCode(int position){
        return portfolioListData.get(position).getCode();
    }

    public ArrayList<PortfolioListData> getPortfolioListData() {
        return portfolioListData;
    }

    public void setPortfolioListData(ArrayList<PortfolioListData> portfolioListData) {
        this.portfolioListData = portfolioListData;
    }
    public void setContext(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("paper",Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        invested=0.00;
    }
    public void buildData(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        portfolioListData = new ArrayList<>();
        sharedPref = context.getSharedPreferences("paper",Context.MODE_PRIVATE);
        orderCode = new ArrayList<>(sharedPref.getStringSet("orderCode", new HashSet<>()));
        orderPrice = new ArrayList<>(sharedPref.getStringSet("orderPrice", new HashSet<>()));
        orderQuant = new ArrayList<>(sharedPref.getStringSet("orderQuant", new HashSet<>()));
        orderDate = new ArrayList<>(sharedPref.getStringSet("orderDate", new HashSet<>()));
        Log.d(TAG, "Order size : " + orderCode.size() +","+ orderPrice.size() +","+
                orderQuant.size()+","+ orderDate.size());
        for(int i=0; i<orderCode.size(); i++){
            getCurrentData(i);
        }
        editor.putFloat("unrealCurrent", invested.floatValue());
        editor.apply();
    }
    private void getCurrentData(int index){
        String url = "https://finance.yahoo.com/quote/" + orderCode.get(index) + ".NS";
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

        price = document.selectXpath(
                "//*[@id=\"nimbus-app\"]/section/section/section/article/section[1]/div[2]/div[1]/section/div/section/div[1]/fin-streamer[1]"
        ).first().text();
        changeNo = document.selectXpath(
                "//*[@id=\"nimbus-app\"]/section/section/section/article/section[1]/div[2]/div[1]/section/div/section/div[1]/fin-streamer[2]"
        ).first().text();
        changePercent = document.selectXpath(
                "//*[@id=\"nimbus-app\"]/section/section/section/article/section[1]/div[2]/div[1]/section/div/section/div[1]/fin-streamer[3]"
        ).first().text();
        changePercent = changePercent.substring(1,changePercent.length()-1);
//        prevClose = document.selectXpath("//*[@id=\"quote-summary\"]/div[1]/table/tbody/tr[1]/td[2]").first().text();

        portfolioListData.add(new PortfolioListData(
                orderCode.get(index),
                price,
                changeNo,
                changePercent,
                orderPrice.get(index),
                orderQuant.get(index)));
        Double priceDouble = Double.parseDouble(price.replace(",",""));
        Integer quantInt = Integer.parseInt(orderQuant.get(index));
        invested += priceDouble*quantInt;
    }
    public void clearData(){
        orderCode = new ArrayList<>();
        orderPrice = new ArrayList<>();
        orderQuant = new ArrayList<>();
        orderDate = new ArrayList<>();
        orderCode.clear();
        orderPrice.clear();
        orderQuant.clear();
        orderDate.clear();
        editor.putStringSet("orderCode", new HashSet<>(orderCode));
        editor.putStringSet("orderPrice", new HashSet<>(orderPrice));
        editor.putStringSet("orderQuant", new HashSet<>(orderQuant));
        editor.putStringSet("orderDate", new HashSet<>(orderDate));
        editor.apply();
    }
}