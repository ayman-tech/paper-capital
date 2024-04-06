package com.example.papertrading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StockListAdapter extends ArrayAdapter<StockListData> {

    private TextView codeTV, priceTV, changeTV;
    public StockListAdapter(@NonNull Context context, ArrayList<StockListData> stockListData) {
        super(context, R.layout.stock_list, stockListData);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        StockListData stockListData = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_list, parent, false);
        }
        codeTV = convertView.findViewById(R.id.codeStockListTextView);
        priceTV = convertView.findViewById(R.id.priceStockListTextView);
        changeTV = convertView.findViewById(R.id.changeStockListTextView);

        codeTV.setText(stockListData.getCode());
        priceTV.setText(stockListData.getPrice());
        changeTV.setText(stockListData.getChangeNo() + " | " + stockListData.getChangePercent());

        return convertView;
    }

}
