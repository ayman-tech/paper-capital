package com.example.papertrading.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.papertrading.StockListData;

import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel {

//    private final MutableLiveData<String> mText;
    private ArrayList<StockListData> stockListData;
    public NotificationsViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is notifications fragment");
        stockListData = new ArrayList<>();
        stockListData.add(new StockListData("BRITANNIA", "2000", "+200", "+10%"));
    }

    public ArrayList<StockListData> getStockListData() {
        return stockListData;
    }
    public String getStockDataCode(int position){
        return stockListData.get(position).getCode();
    }
    public void setStockListData(ArrayList<StockListData> stockListData){
        this.stockListData = stockListData;
    }
//    public LiveData<String> getText() {
//        return mText;
//    }
}