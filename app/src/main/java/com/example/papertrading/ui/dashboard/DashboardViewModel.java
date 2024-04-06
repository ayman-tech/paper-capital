package com.example.papertrading.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

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
    private final String TAG = "Dashboard ViewModel";
    private String price, changeNo, changePercent;
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

        price = document.selectXpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/fin-streamer[1]").first().text();
        changeNo = document.selectXpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/fin-streamer[2]").first().text();
        changePercent = document.selectXpath("//*[@id=\"quote-header-info\"]/div[3]/div[1]/div/fin-streamer[3]").first().text();
//        prevClose = document.selectXpath("//*[@id=\"quote-summary\"]/div[1]/table/tbody/tr[1]/td[2]").first().text();

        portfolioListData.add(new PortfolioListData(
                orderCode.get(index),
                price,
                changeNo,
                changePercent,
                orderPrice.get(index),
                orderQuant.get(index)));
    }
    public void clearData(){
        sharedPref = context.getSharedPreferences("paper",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
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